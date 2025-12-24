# 📱 Device Life
> **"수많은 디바이스 중 유저에게 최적의 조합을 제공하다"**
> UMC 9th Project - Team Device Life

## 📖 Project Overview
**Device Life**는 사용자가 자신만의 전자기기 조합을 만들고, AI 기반의 실시간 평가(호환성, 충전 편의성, 컬러 매칭)를 통해 최적의 기기 생태계를 구성하도록 돕는 웹 서비스입니다.

### ✨ 핵심 기능 (Key Features)
* [cite_start]조합 생성 및 실시간 평가: 기기 탐색을 통해 기기를 추가/삭제하고 나만의 조합을 구성 [cite: 1]
* [cite_start]실시간 평가: 기기 변경 시마다 생태계 호환성, 충전 편의성, 컬러 매칭에 대한 점수와 요약을 즉시 제공 [cite: 2]
* [cite_start]기기 탐색 및 필터: 가격대 설정을 위한 레인지 슬라이더와 카테고리, 브랜드 필터를 통해 원하는 기기를 검색 [cite: 5, 6]
* [cite_start]라이프스타일 AI: 주된 용도와 선호 브랜드, 중요 요소(가성비, 휴대성 등)를 시각적으로 선택하여 맞춤형 평가 기준 설정 [cite: 15, 17]
* [cite_start]내 조합 라이브러리: 저장된 조합을 관리하며, 스와이프 삭제 및 휴지통 복구 기능 제공 [cite: 7, 9, 19]

---

## 🛠️ 기술 스택 (Tech Stack)

* Framework: 미정
* Language: 미정
* Styling: 미정
* Build Tool: 미정
* Package Manager: 미정

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
* 예시: feature/1-login

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
