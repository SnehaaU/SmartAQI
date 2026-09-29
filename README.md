# SmartAQI — Air Quality Monitoring System

A simple web application to check the Air Quality Index (AQI) of cities. Data is stored manually in MySQL (no external APIs). Built with **Java Servlets**, **JDBC**, **MySQL**, and **HTML/CSS**.

## Features

- User registration and login (session-based)
- Admin login (`admin@smartaqi.com` / `admin123`)
- City AQI lookup from database
- Categories: Good, Moderate, Poor, Hazardous
- Health suggestions based on AQI level
- Bar chart showing 7-day AQI trend (Chart.js)
- PM2.5 and PM10 display

## Project Structure

```
smartaqi/
├── index.html          # Home page
├── login.html          # User login
├── register.html       # User registration
├── dashboard.html      # AQI search & charts
├── admin.html          # Admin login
├── database/
│   └── schema.sql      # MySQL tables + sample data
├── backend/
│   ├── src/com/smartaqi/   # Java source (servlets, DAO, JDBC)
│   └── mysql-connector-j-9.7.0.jar
├── WEB-INF/
│   ├── web.xml
│   ├── classes/        # Compiled .class files (after build)
│   └── lib/            # JDBC driver
└── compile.bat         # Windows compile script
```

## Prerequisites

- Java JDK 11+
- Apache Tomcat 9.x (uses `javax.servlet`)
- MySQL 8.0+

## Setup

### 1. MySQL Database

1. Start MySQL.
2. Update password in `backend/src/com/smartaqi/DatabaseConnection.java`:
   ```java
   private static final String PASSWORD = "your_mysql_password";
   ```
3. Import schema:
   ```bash
   mysql -u root -p < database/schema.sql
   ```

### 2. Compile Java Backend

```bat
set CATALINA_HOME=C:\path\to\apache-tomcat-9.0.xx
compile.bat
```

### 3. Deploy to Tomcat

Copy the entire `smartaqi` folder to:

```
%CATALINA_HOME%\webapps\SmartAQI
```

Start Tomcat, then open:

```
http://localhost:8080/SmartAQI/
```

## API Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/register` | Register new user |
| POST | `/api/login` | Login (creates session) |
| POST | `/api/logout` | Logout |
| GET | `/api/user` | Current logged-in user |
| GET | `/api/cities` | List cities (login required) |
| GET | `/api/aqi?city=Delhi` | Get AQI for city (login required) |

## Default Credentials

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@smartaqi.com | admin123 |

Register new users via the Sign Up page.

## Sample Cities in Database

Delhi, Mumbai, Bangalore, Chennai, Kolkata, Hyderabad, Pune, Jaipur

## System Modules (as per specification)

1. **Authentication** — RegisterServlet, LoginServlet, sessions
2. **User Interface** — HTML pages with city search
3. **AQI Data Management** — `aqi_data` table in MySQL
4. **AQI Retrieval** — AQIServlet + AQIDAO (JDBC)
5. **Display** — Dashboard with values, categories, Chart.js graph
6. **Database** — MySQL for users and AQI records

## Future Enhancements

- Real-time API integration
- Admin panel to add/edit AQI records
- AQI predictions using historical data
