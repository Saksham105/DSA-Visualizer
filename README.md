# DSA-Visualizer
## Running The Project
### Build and test the Java modules

Run from the repository root:

```bash
mvn clean install
```

To compile the custom collection tests without running the full reactor:

```bash
cd custom-collections
mvn test-compile
```

The normal Maven test command is preferred for executing the JUnit suite:

```bash
cd custom-collections\src
java -cp target\classes;target\test-classes com.dsa.collections.linear.*
```

### Start the backend

From the repository root:

```bash
mvn spring-boot:run -pl dsa-backend
```

The frontend development proxy expects the backend at `http://localhost:8080`.

### Start the frontend

```bash
cd dsa-frontend
npm install
npm run dev
```

Vite serves the application at its configured development port, `5173`, and forwards `/api` calls to the backend.



## Inputs to improve professional
- [DONE] beginner friendly names for each DS in frontend. (easy)
- [DONE] backend layers. (medium)
- [DONE] description ui change  (medium)
- brief description  (medium)
- remove all developement process related comments (linked ilst, queue) (medium)
- highlight failed case for each DS all ops.(medium)
- separate documentation files/folder.(hard)
- dummy addresses to array, list, nodes.(medium)
- Deep understanding of project and Architecture
- Database addition
- tree collision removing is not that easy. on hold. (HARD)