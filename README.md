# 입출금통장 개설 프로세스
## 이체인증기능 구현하기

### 1. 요구사항
* 단어인증을 위해, 타행으로 1원과 4글자 메시지를 보냅니다.
* 이체 시, 두번 중복 이체하지 않도록 처리합니다.
* 이체인증시, 타행에서 응답을 주지 않았을 경우, 후속처리가 감안되어야 합니다.

### 2. 설계 및 구현
#### 2.1 중복이체 방지
* 임시 캐시 저장소를 만들어, 처음 들어온 요청을 저장, 두번째 들어온 요청부터 이체를 막고 익셉션 처리합니다.
* 임시 캐시 저장소는 HashMap 으로 메모리에 올려 체크 했습니다.
    * memcached나 Redis 같은 서비스를 같이 사용해야 합니다.  
 
#### 2.2 이체인증 프로세스
* 이체인증은 비동기로 처리됩니다.
* Queue로 메모리에 이체 요청을 올려 처리하고 있습니다. 
    * 레빗엠큐 와같은 메시징 큐를 사용해야 합니다.
* 만약 타임아웃등으로 이체에 실패 한 경우, 실제 1원이 타행으로 이체 됐는지 안됐는지 조회를 통해 확인해야합니다.
    * 음답을 주진 않았고 1원 전송 실패가 확인된다면 재이체전송을 통해 다시 1원을 이체할 수 있도록 합니다.
    * 음당을 주진않았지만 1원은 성공적으로 전송했다면, 다음단계로 전달할 수 있도록 진행합니다. 이때 이력은 실패로 남습니다.
    * 조회마저 실패한다면..? sms로 사용자에 이체 실패를 알리고 주기적으로 조회 요청을 통해 확인해야겠습니다..
#### 2.3 히스토리
* 모든 요청의 성공/실패에 대한 히스토리를 저장합니다.

### 3. 실행
~~~
조건 : 계좌번호가 123일 경우에는 무조건 성공 메세지를 반환합니다.
그외에는 모두 실패 메세지를 반환합니다.,

Content-Type : application/json
requestURL: http://localhost:8080/api/transfer-auth 
~~~


#### 3.1 실패 케이스
* 요청
```json
{
    "requestUserUUID" :"3456788",
    "userId": "kiomnd2",
    "userName": "홍길동",
    "accountNo": "111123456789",
    "bankCode": "123"
}
```
* 응답
~~~json
{
    "requestUserUUID": "223456788",
    "transferUUID": "928d6ebc-9838-4595-bcc5-44b7969627cf",
    "requestDate": "2020-08-09T01:37:11.626923",
    "responseDate": "2020-08-09T01:37:11.636455",
    "error": true,
    "message": "응답에는 실패했즈만 1원 이체 성공을 확인했습니다.",
    "resultType": "FAIL"
}
~~~



#### 3.2 성공 케이스

* 요청
~~~json
{
    "requestUserUUID" :"1231111188",
    "userId": "kiomnd2",
    "userName": "홍길동",
    "accountNo": "12345249",
    "bankCode": "123"
}
~~~

* 실패
~~~json
{
    "requestUserUUID": "1231111188",
    "transferUUID": "413ca4ab-1a17-4d9a-87e9-d67cae803ee6",
    "requestDate": "2020-08-09T01:41:02.233033",
    "responseDate": "2020-08-09T01:41:02.235069",
    "error": false,
    "message": "이체에 성공하였습니다",
    "resultType": "SUCCESS"
}
~~~



#### 4. 차후 구현
* 레디스를 사용한 임시 캐싱 싱 
* 레빗엠큐를 사용한 비동기 메시징 처리
