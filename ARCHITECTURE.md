# DSA Visualizer — Project Architecture

The project has three main parts:

1. **Frontend** — lets users choose a data structure, select operations, and watch the steps.
2. **Backend** — runs those operations and records what happened at each step.
3. **Custom Collections** — contains the Java implementations of the data structures.

The frontend does not run the data-structure algorithms itself. It sends the requested operations to the backend. The backend runs them using the custom collections and sends back a step-by-step result for the frontend to display.

## Main architecture

┌──────────────────────────────── FRONTEND ──────────────────────────────────┐
│                                                                            │
│  Home → Choose a data structure → Choose operations → Execute              │
│                                                    │                       │
│  Workspace holds the operation queue and response  │                       │
│  Playback controls select one step at a time       │                       │
│  Structure-specific OutputWindow displays that step│                       │
└────────────────────────────────────────────────────┼───────────────────────┘
                                                     │
                              POST /api/{structure}/execute
                              JSON: { "operations": [...] }
                                                     │
                                                     ▼
┌──────────────────────────────── BACKEND ──────────────────────────────────┐
│  Controller receives the request as ExecutionRequest                      │
│       ↓                                                                   │
│  Controller processes each Operation in order                             │
│       ↓                                                                   │
│  Traced class calls the matching custom collection and records each step  │
│       ↓                                                                   │
│  StepRecorder builds the trace → ExecutionResponse                        │
└────────────────────────────────────────────────────┼──────────────────────┘
                                                     │
                              JSON: { "steps": [...], "totalSteps": n }
                                                     │
                                                     ▼
                                  FRONTEND stores it as `trace`
                                                     │
                                  `useStepPlayer` picks a step
                                                     │
                                  OutputWindow displays that step

## Frontend structure

The frontend is a React application built with Vite. Its source is in `dsa-frontend/src`.

src/
├── main.jsx
├── App.jsx
├── pages/
│   └── Home.jsx
├── shared/
│   ├── api/
│   │   └── client.js
│   ├── components/
│   ├── constants/
│   └── hooks/
└── modules/
    ├── arraylist/
    ├── linkedlist/
    ├── stack/
    ├── queue/
    ├── hashset/
    └── treemap/

### Starting and choosing a module

`main.jsx` starts the React app. `App.jsx` decides what to show: the home screen or one of the six workspaces. Navigation is handled with React state; the application does not currently use URL routes.

`Home.jsx` shows the available data structures. When the user picks one, `App` displays its workspace.

### What a workspace does

Each structure has its own workspace, such as `ArrayListWorkspace.jsx`. A workspace brings together the shared controls and that structure’s own visual display.

It keeps track of:

- **`queue`** — the operations the user has selected.
- **`trace`** — the steps returned by the backend.
- **`isLoading` and `error`** — request status and any error message.
- **The current playback position** — managed by `useStepPlayer`.

When the user presses Execute, the workspace sends its queued operations to its module’s API function. When a response arrives, the workspace stores it in `trace` and passes the current step to its `OutputWindow`.

### Shared frontend components

Components under `shared/components` are used by more than one structure:

| Component             | What it does                                                      |
|-----------------------|-------------------------------------------------------------------|
| `OperationPicker`     | Shows the operations available for the selected structure.        |
| `ArgumentForm`        | Collects the values or indexes needed by an operation.            |
| `OperationQueueList`  | Shows queued operations and lets the user remove one.             |
| `BackExecuteBar`      | Provides the Back and Execute buttons.                            |
| `PlaybackControls`    | Lets the user play, pause, move between steps, and change speed.  |
| `WorkspaceHeader`     | Shows the selected structure’s name and module number.            |
| `Button`              | Provides shared button styling.                                   |

The operation buttons and their form fields come from each module’s `config/operationsConfig.js`. For example, the `ArrayList operation config` describes the operation name and what values its form should ask the user for.

`useStepPlayer.js` handles playback. It moves through the response’s `steps` list. It does not need to know how an array, stack, or tree works.

### Structure-specific displays

Each module has an `OutputWindow` that displays the current step in a way that suits its data structure:

| Data structure  | Frontend display                                            |
|-----------------|-------------------------------------------------------------|
| ArrayList       | Shows values as indexed boxes.                              |
| LinkedList      | Shows nodes joined together, with null markers at the ends. |
| Stack           | Shows boxes with the last item at the top.                  |
| Queue           | Shows connected items with front and rear labels.           |
|                 | It reuses LinkedList display components.                    |
| HashSet         | Shows table slots, including empty and deleted slots.       |
| TreeMap         | Draws a tree. The user can click a node to show its value.  |

For example, the `TreeMap OutputWindow` uses `TreeCanvas`. The canvas uses `layoutTree.js` to arrange the tree for display.

## Frontend and backend mapping

For every structure, the frontend sends an operation list to one matching backend route. The route’s controller creates the traced class, which uses the corresponding collection implementation.

| Frontend workspace | API route | Backend controller | Traced class → custom collection |
|---|---|---|---|
| `ArrayListWorkspace` | `/api/arraylist/execute` | `JArrayListController` | `TracedJArrayList` → `JArrayList` |
| `LinkedListWorkspace` | `/api/linkedlist/execute` | `JLinkedListController` | `TracedJLinkedList` → `JLinkedList` |
| `StackWorkspace` | `/api/stack/execute` | `JStackController` | `TracedJStack` → `JStack` |
| `QueueWorkspace` | `/api/queue/execute` | `JQueueController` | `TracedJQueue` → `JLinkedQueue` |
| `HashSetWorkspace` | `/api/hashset/execute` | `JHashSetController` | `TracedJHashSet` → `JHashSet` |
| `TreeMapWorkspace` | `/api/treemap/execute` | `JTreeMapController` | `TracedJTreeMap` → `JTreeMap` |

## What data moves between the frontend and backend?

### Frontend sends operations

Suppose the user adds the value `42`. The frontend creates an operation object:

```json
{ "name": "add", "arguments": [42] }
```

That object is added to the workspace’s `queue`. On Execute, `shared/api/client.js` sends the queue in a request like this:

```json
{
  "operations": [
    { "name": "add", "arguments": [42] }
  ]
}
```

The backend receives it as `ExecutionRequest`, which holds a list of `Operation` objects. An `Operation` holds its `name` and `arguments`.

### Backend runs operations and records steps

The matching controller reads the operations in order and calls the matching method on its traced class. The traced class calls the custom collection and records what happened.

The backend’s `StepRecorder` collects `Step` objects. A step includes:

- `stepType` — for example, `ADD`, `REMOVE`, or `COMPARE`.
- `snapshot` — the data structure’s state for that step.
- `highlightedIndices` — the positions the frontend should highlight.
- `helpers` — extra values that some algorithms need to show.
- `description` — a short explanation displayed with the step.

At the end, the controller returns an `ExecutionResponse` containing the list of `steps` and `totalSteps`.

### Frontend plays the response

The workspace stores the response in `trace`. `useStepPlayer` chooses which step is current. The module’s `OutputWindow` reads that step and displays its snapshot, description, and highlights.

Frontend queue
    { name, arguments }
          ↓
ExecutionRequest
    operations: [Operation]
          ↓
Controller → traced class → custom collection
          ↓
ExecutionResponse
    steps: [Step], totalSteps
          ↓
Workspace stores response as `trace`
          ↓
useStepPlayer selects the current step
          ↓
Module OutputWindow displays it

## A few useful details

- Each Execute request creates a **new collection in the backend**. Operations in the same request run in order on that collection, but the collection is not kept for the next request.
- In development, Vite forwards `/api` requests to the backend at `http://localhost:8080`. This is configured in `vite.config.js`.
- The shared client can also use `VITE_API_BASE_URL` when the frontend and backend are hosted separately.
- The frontend’s step type names must match the backend’s `StepType`.