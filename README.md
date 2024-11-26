# PJT-FINAL

# URS

A platform that recommends iconic walking routes featured in K-content, offering an immersive exploration experience for users.

---

## Background & Purpose
This project was started to address the growing demand for accessible walking routes that highlight famous locations from K-content. It aims to provide users with an engaging way to explore iconic spots featured in movies, dramas, and other K-content, while offering customized route recommendations and relevant local information to enhance their experience.

## Key Features
- **Feature 1**: Route guidance using Kakao Maps
- **Feature 2**: Option to select a walking route
- **Feature 3**: Verification through image upload
- **Feature 4**: Recognizing main locations using Google Maps Street View

## Tech Stack

- **Frontend**: Vue.js, Vue Router, Pinia, Axios
- **Backend**: Spring Boot, JPA, RESTful API
- **Database**: MySQL
- **Others**: Lombok, Kakao Map API, KMA (Korea Meteorological Administration) API, etc.

## Installation & Setup

### Frontend (Vue.js)

```bash
# Navigate to the frontend directory
cd frontend

# Install dependencies
npm install

# Run the frontend server
npm run serve
```

### Backend (Spring Boot)

```bash
# Navigate to the backend directory
cd backend

# Run the backend server
./mvnw spring-boot:run
```

## Project Structure

```
FINAL-PROJECT/
├── frontend/               # Vue.js frontend project
│   ├── src/
│   │   ├── components/
│   │   ├── views/
│   │   ├── store/
│   │   └── main.js
├── backend/                # Spring Boot backend project
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com.ssafy.urs/
│   │   │   └── resources/
│   └── pom.xml
├── README.md
└── .gitignore
```

- **frontend/**: Contains the Vue.js frontend project
- **backend/**: Contains the Spring Boot backend project

## Contributing
### Commit Message Format
```plaintext
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

### Subject `<subject>`
- Start with a capital letter, keep within 50 characters, and avoid a period (`.`) at the end.
- Use imperative mood.
- Add a blank line between the subject and the body.

### Body `<body>`
- Break lines at 72 characters.
- Focus on explaining "what" and "why" the change was made, using present tense.

### Type `<type>`
- **feat**: Add a new feature
- **fix**: Fix a bug
- **docs**: Documentation changes
- **style**: Code style changes (formatting, missing semicolons, etc., no impact on logic)
- **refactor**: Code refactoring (no functional changes)
- **test**: Adding or updating tests
- **chore**: Maintenance tasks

### Footer `<footer>`
- **Breaking Changes**: Start with `BREAKING CHANGE:` and provide a description of the change, reason, and migration instructions.
- **Referencing Issues**: Reference related issues with `Closes #123` format.

### Example
```plaintext
feat(Auth): Add token-based authentication

Introduce JWT token authentication for improved security and scalability.

BREAKING CHANGE: Session-based auth removed; clients must adopt token-based auth.
```

## Troubleshooting

### Common Issues

- **Issue 1**: Tried to handle it on the front-end when using the KMA (Korea Meteorological Administration) API, but faced a CORS issue, so resolved it by handling it in the backend.
- **Issue 2**: Wanted to show three places at once using Google Maps API's street view, but couldn't succeed due to the lack of a dedicated library.
- **Issue 3**: Solved updating the average rating of a route whenever a new review is added by using a trigger.
- **Issue 4**: Wanted to show weather information for the selected district when choosing from a dropdown. Instead of calculating it, resolved it by inputting the coordinates.
- **Issue 5**: Wanted to implement login and logout using Spring Security but found its underlying mechanisms too complex, leading to failure. Instead, implemented login, session maintenance, and logout using cookies and sessions.


## Authors & Contact

- **Author**: [Jinho Son](https://github.com/SON1205)
- **Email**: jjinoson1205@gmail.com
