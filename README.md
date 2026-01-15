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

## 🧑🏻‍💻 팀원 (Contributors)

| <img src="https://github.com/weeeeestern.png" width="150" height="150"/> | <img src="https://github.com/nsh0919.png" width="150" height="150"/> | <img src="https://github.com/LABYRINTH3.png" width="150" height="150"/> | <img src="https://github.com/ccjngwn.png" width="150" height="150"/> |
| :---------------------------------------------------------------: | :---------------------------------------------------------------: | :---------------------------------------------------------------: | :---------------------------------------------------------------: |
| 박은서 <br/> [@weeeeestern](https://github.com/weeeeestern) | 남성현 <br/> [@nsh0919](https://github.com/nsh0919) | 이태훈 <br/> [@LABYRINTH3](https://github.com/LABYRINTH3) | 채정원 <br/> [@ccjngwn](https://github.com/ccjngwn) |
| **Backend Lead** | **Backend** | **Backend** | **Backend** |

---

## 🛠️ 기술 스택 (Tech Stack)

| 역할 | 종류 | 선정 근거 |
| :--: | :-- | :-- |
| **Backend** | ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) | 안정적인 서버 아키텍처를 구성하고 REST API 개발을 효율적으로 수행하기 위해 사용했습니다. |
| **Database** | ![Amazon RDS](https://img.shields.io/badge/Amazon%20RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white) ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white) | RDS(MySQL)로 영속 데이터를 안정적으로 관리하고, VPC Private Subnet에 배치해 보안성을 높였습니다. |
| **Infrastructure** | ![AWS](https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white) ![Amazon EC2](https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white) ![Amazon Route 53](https://img.shields.io/badge/Amazon%20Route%2053-8C4FFF?style=for-the-badge&logo=amazonroute53&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![Nginx](https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white) | 단일 EC2 내 Docker 컨테이너(Nginx + Spring Boot)로 배포를 단순화하고, Route53으로 도메인 라우팅을 구성했습니다. Nginx는 리버스 프록시/HTTPS 종단 및 라우팅을 담당합니다. |
| **Async / Queue** | ![Amazon SQS](https://img.shields.io/badge/Amazon%20SQS-FF4F8B?style=for-the-badge&logo=amazonsqs&logoColor=white) | 평가 작업을 SQS(Job/Result Queue)로 분리해 처리량 변동을 흡수하고, On-Prem Evaluation Worker와 API 서버를 느슨하게 결합했습니다. |
| **External Integration** | ![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white) | 운영(관리자 콘솔) 기능을 Discord Slash Command로 대체하고, Spring Boot 앱 내부에서 JDA 리스너로 이벤트를 처리합니다. |
| **API Docs** | ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black) | API 문서 자동화를 통해 프론트엔드와의 협업 효율을 높이고, 명세 관리 부담을 줄였습니다. |
| **Collaboration / CI** | ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white) ![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white) | GitHub 기반으로 협업을 진행하고, GitHub Actions로 CI 파이프라인을 구성해 자동화된 빌드/배포 환경을 구축했습니다. |

---

## 🏗️ 인프라 구조 (Infrastructure Architecture)
![img.png](img.png)
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
