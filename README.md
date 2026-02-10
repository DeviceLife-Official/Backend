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
|:------------------------------------------------------------------------:|:--------------------------------------------------------------------:|:-----------------------------------------------------------------------:|:--------------------------------------------------------------------:|
|         박은서 <br/> [@weeeeestern](https://github.com/weeeeestern)         |           남성현 <br/> [@nsh0919](https://github.com/nsh0919)           |         이태훈 <br/> [@LABYRINTH3](https://github.com/LABYRINTH3)          |           채정원 <br/> [@ccjngwn](https://github.com/ccjngwn)           |
|                             **Backend Lead**                             |                             **Backend**                              |                               **Backend**                               |                             **Backend**                              |
|                        온보딩, 조합 평가 로직, 시스템 설계 및 배포                        |                       인증, 조합 평가 로직, 배포 및 CICD                        |                         기기 데이터 크롤링, 조합 관리, 휴지통                          |                        이미지 데이터 크롤링, 고객 지원 콘텐츠                        |

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

> **💡 핵심 아키텍처 포인트 (Key Architecture Decisions)**
> 
> **1. 비동기/이벤트 기반 아키텍처 (Event-Driven Architecture with SQS)**
> * **도입 배경:** 디바이스 호환성 평가 알고리즘은 복잡한 연산을 포함하고 있어, 동기 처리 시 API 응답 속도 저하와 타임아웃 위험이 있었습니다.
> * **해결:** **AWS SQS**를 도입하여 평가 작업을 큐(Queue)로 분리하고, 별도의 **Worker** 서버가 이를 비동기로 처리하도록 설계했습니다. 이를 통해 **API 서버의 부하를 격리하고 트래픽 급증 시에도 안정적인 사용자 경험**을 보장합니다.
> * 👉 **[Evaluation Worker Repository](https://github.com/DeviceLife-Official/Worker)**
>
>
> **2. Tailscale 기반의 제로 트러스트 보안망 (Secure Internal Network)**
> * **도입 배경:** 클라우드(AWS EC2)와 온프레미스/별도 VM(Worker) 간의 통신을 위해 공용 인터넷망에 포트를 개방하는 것은 보안상 취약점이 될 수 있었습니다.
> * **해결:** **Tailscale VPN**을 활용하여 외부망과 격리된 **암호화 사설망(Overlay Network)**을 구축했습니다. **Backend(8080 포트)를 Public IP로 개방하지 않고도** 안전하게 Worker와 통신할 수 있는 강력한 보안 환경을 구성했습니다.
>
>
> **3. Discord 기반의 ChatOps 환경 구축 (Server-side Admin Console via Discord)**
> * **도입 배경:** 프로젝트 초기 단계에서 별도의 웹 기반 Admin(관리자) 페이지를 구축하는 것은 개발 리소스 낭비가 큽니다. 하지만 팀원들이 '추천 세트' 데이터를 조회하고 수정할 수 있는 공통의 운영 도구가 절실했습니다.
> * **해결:** **Spring Boot 애플리케이션 내부에 JDA(Java Discord API)를 내장**하여, Discord를 실시간 운영 콘솔(Ops Console)로 변모시켰습니다.
> * **Slash Command 활용:** `/device-search`(기기 조회), `/featured-set`(추천 세트 등록) 등의 명령어를 통해 DB에 직접 접근하여 운영 데이터를 제어합니다.
> * **운영 효율성:** 프론트엔드 개발 없이도 운영팀(팀원)이 Discord 채널에서 실시간으로 데이터를 검증(QA)하고, **Role 기반 권한 관리**를 통해 안전하게 운영 액션을 수행하는 **ChatOps** 환경을 구현했습니다.
>
>
>
>
---

## 🌐 Git-flow 전략 (Git-flow Strategy)

* main: 최종적으로 사용자에게 배포되는 가장 안정적인 버전 브랜치
* feature: 기능 개발용 브랜치. main에서 분기하여 작업

---

## 📌 브랜치 규칙 및 네이밍 (Branch Rules & Naming)

* 작업 시작 전, 항상 최신 main 내용 받아오기 (git pull origin main)
* 작업 완료 후, main으로 Pull Request(PR) 생성
* 리뷰 필료한 PR은 Reviewer 설정
* 브랜치 이름 형식: feat/기능명
* 예시: feat/combination-api

---

## 🎯 컨벤션 (Convention)
<aside>
PR/ISSUE 제목 형식: [Feat] 로그인 api 개발
</aside>

### 📋 타입 목록

| type | 설명 |
| :--- | :--- |
| `Feat` | 새로운 기능을 추가할 경우 |
| `Fix` | 버그를 고친 경우 |
| `Hotfix` | 급하게 치명적인 버그를 고쳐야하는 경우 |
| `Style` | 코드 포맷 변경, 세미 콜론 누락, 코드 수정이 없는 경우 |
| `Refactor` | 프로덕션 코드 리팩토링 |
| `Comment` | 필요한 주석 추가 및 변경 |
| `Docs` | 문서를 수정한 경우 |
| `Test` | 테스트 추가, 테스트 리팩토링(프로덕션 코드 변경 X) |
| `CI` | CICD 관련 작업 |
| `Config` | 설정 파일 작성 및 수정 |
| `Security` | 권한 수정 |
| `Chore` | 빌드 태스트 업데이트, 패키지 매니저를 설정하는 경우(프로덕션 코드 변경 X) |
| `Rename` | 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우 |
| `Remove` | 파일을 삭제하는 작업만 수행한 경우 |
