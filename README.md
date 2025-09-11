# 📌telo-back-refactor

이 저장소는 [원본 Telo 프로젝트](https://github.com/2024OSS-Telo/Telo_spring)를 기반으로,
**성능 최적화 및 코드 구조 개선**을 위해 리팩토링한 버전입니다.

### 🔑주요 개선 사항

* **API 성능 개선**
  * 단일 조회 성능: 0.38초 → 0.21초 (약 1.8배 향상)
  * 목록 조회(10,000건): 8.4초 → 0.16초 (약 46배 향상)
  
* **아키텍처 개선**
  * 계층형 구조 → 도메인형 구조로 전환
  * 응집도 및 유지보수성 강화
    
* **DB 최적화**
  * N+1 문제 해결 (`FetchType.LAZY`, `JOIN FETCH`, `@EntityGraph`)
  * 인덱스 적용 및 UUID 관리 효율화
    
* **불필요한 암호화 로직 제거**
  * 서비스 요구사항 검토 후, 필요 없는 민감정보 필드 제거

### 🔗 관련 링크
* [원본 프로젝트 (Legacy Code)](https://github.com/2024OSS-Telo)
* [블로그 글 (리팩토링 과정 정리)](https://velog.io/@sihejt/%ED%85%94%EB%A1%9C-%EB%B0%B1%EC%97%94%EB%93%9C-%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81-API-%EC%84%B1%EB%8A%A5-%EB%B0%8F-%EC%BD%94%EB%93%9C-%EA%B5%AC%EC%A1%B0-%EA%B0%9C%EC%84%A0-%EA%B8%B0%EB%A1%9D)
