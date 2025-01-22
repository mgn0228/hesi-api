# API Server
### 구성
- Spring Boot : 3.4.1
- Java : 21.x.x
- Build : Gradle
- IDE : IntelliJ
- Dababase
  - MariaDB
  - MongoDB : 로그인 시도 이력 저장
- DB 쿼리 작성
  - JDBC
  - JPA
  - QueryDSL
- 로그인 처리
  - Spring Security
  - JWT
- API 명세서 작성
  - Swagger
---
### A회사 상황
- A회사의 협력업체들은 작업할 때 매일, 매주 또는 필요할 떄 작성해야 하는 문서들이 있는데 양식 통일이 안되어 있음(한글, 엑셀 그림판 등)
- 협력업체들 문서 결재를 받기 위해 A업체를 직접 방문해야함
### A회사 요구사항
- 협력업체들이 작성한 문서들을 하나의 웹사이트에서 관리하기
- 협력업체들이 업로드한 문서들 온라인 결재
---
### 구현
1. 로그인
2. 문서 작성 및 저장
3. 작성된 문서 결재자 지정 후 결재 문서로 저장
4. 결재 진행
### 설명
https://chiseled-rook-f8d.notion.site/18339fabda35800fa306f51f4c50bf97
