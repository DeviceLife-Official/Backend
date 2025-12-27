# 📱Device Life - Backend
> 수많은 디바이스 중 유저에게 최적의 조합을 제공하다
> UMC 9th Project - Server Repository

## 📖 프로젝트 소개 (Project Overview)
이 저장소는 Device Life의 API 서버 및 데이터베이스를 관리합니다. 기기 데이터 관리, 조합 저장 로직, 그리고 핵심 기능인 '실시간 호환성 평가 알고리즘' 처리를 담당합니다.

### ✨ 백엔드 핵심 기능 (Key Features)
* RESTful API 설계: 클라이언트와 데이터를 주고받기 위한 명세 기반 API 구현
* 평가 알고리즘 로직: 기기 조합에 따른 생태계 점수, 충전 호환성, 컬러 매칭 산식 구현
* 데이터베이스 구축: 스마트폰, 노트북 등 방대한 기기 정보 및 브랜드 데이터 모델링
* 사용자 데이터 관리: 유저별 조합 저장, 즐겨찾기, 휴지통(Soft Delete) 기능 처리
* 라이프스타일 가중치: 사용자 입력 데이터에 따라 평가 점수를 보정하는 로직 처리

---

## 🛠️ 기술 스택 (Tech Stack)

Backend : Spring Boot
Database : MySQL (필요 시 NoSQL 추가)
Infra : AWS / Docker / Nginx
Collaboration : GitHub / GitHub Actions
API Docs : Swagger
Async / Cache : 필요 시 Redis / Message Queue 추가

---

## 🌐 Git-flow 전략 (Git-flow Strategy)

* main: 최종적으로 사용자에게 배포되는 가장 안정적인 버전 브랜치
* develop: 다음 출시 버전을 개발하는 중심 브랜치. 기능 개발 완료 후 feature 브랜치들이 병합
* feature: 기능 개발용 브랜치. develop에서 분기하여 작업

---

## 📌 브랜치 규칙 및 네이밍 (Branch Rules & Naming)

* 모든 기능 개발은 feature 브랜치에서 시작
* 작업 시작 전, 항상 최신 develop 내용 받아오기 (git pull origin develop)
* 작업 완료 후, develop으로 Pull Request(PR) 생성
* PR에 Reviewer(멘션) 지정 이후 머지
* 브랜치 이름 형식: feature/이슈번호-기능명
* 예시: feature/2-combination-api

---

## 🎯 커밋 컨벤션 (Commit Convention)

### 주의 사항
* type은 소문자만 사용 (feat, fix, refactor, docs, style, test, chore)
* subject는 모두 현재형 동사

### 📋 타입 목록

| type | 설명 |
| :--- | :--- |
| start | 새로운 프로젝트를 시작할 때 |
| feat | 새로운 기능을 추가할 때 |
| fix | 버그를 수정할 때 |
| design | CSS 등 사용자 UI 디자인을 변경할 때 |
| refactor | 기능 변경 없이 코드를 리팩토링할 때 |
| settings | 설정 파일을 변경할 때 |
| comment | 필요한 주석을 추가하거나 변경할 때 |
| dependency/Plugin | 의존성/플러그인을 추가할 때 |
| docs | README.md 등 문서를 수정할 때 |
| merge | 브랜치를 병합할 때 |
| deploy | 빌드 및 배포 관련 작업을 할 때 |
| rename | 파일 혹은 폴더명을 수정하거나 옮길 때 |
| remove | 파일을 삭제하는 작업만 수행했을 때 |
| revert | 이전 버전으로 롤백할 때 |

### ✨ 예시
* feat: 컴포넌트 추가
* fix: 가려짐 현상 해결
