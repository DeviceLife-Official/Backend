Device Life
수많은 디바이스 중 유저에게 최적의 조합을 제공하다 UMC 9th Project - Team Device Life

📖 프로젝트 소개 (Project Overview)
Device Life는 사용자가 자신만의 전자기기 조합을 만들고, AI 기반의 실시간 평가를 통해 최적의 기기 생태계를 구성하도록 돕는 서비스입니다. 

✨ 핵심 기능 (Key Features)
조합 워크스페이스: 기기 탐색을 통해 기기를 추가/삭제하고 나만의 조합을 구성 

실시간 평가: 기기 변경 시마다 생태계 호환성, 충전 편의성, 컬러 매칭에 대한 점수와 요약을 즉시 제공 

기기 탐색 및 필터: 가격대 설정을 위한 레인지 슬라이더와 카테고리, 브랜드 필터를 통해 원하는 기기를 검색 

라이프스타일 AI: 주된 용도와 선호 브랜드, 중요 요소(가성비, 휴대성 등)를 시각적으로 선택하여 맞춤형 평가 기준 설정 

내 조합 라이브러리: 저장된 조합을 관리하며, 스와이프 삭제 및 휴지통 복구 기능 제공 



🛠️ 기술 스택 (Tech Stack)
Framework: 미정

Language: 미정

Styling: 미정

Build Tool: 미정

Package Manager: 미정

🌐 Git-flow 전략 (Git-flow Strategy)
main: 최종적으로 사용자에게 배포되는 가장 안정적인 버전 브랜치

develop: 다음 출시 버전을 개발하는 중심 브랜치. 기능 개발 완료 후 feature 브랜치들이 병합

feature: 기능 개발용 브랜치. develop에서 분기하여 작업

📌 브랜치 규칙 및 네이밍 (Branch Rules & Naming)
모든 기능 개발은 feature 브랜치에서 시작

작업 시작 전, 항상 최신 develop 내용 받아오기 (git pull origin develop)

작업 완료 후, develop으로 Pull Request(PR) 생성

PR에 Reviewer(멘션) 지정 이후 머지

브랜치 이름 형식: feature/이슈번호-기능명

예시: feature/1-login

🎯 커밋 컨벤션 (Commit Convention)
주의 사항
type은 소문자만 사용 (feat, fix, refactor, docs, style, test, chore)

subject는 모두 현재형 동사
