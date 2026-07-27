# DSA-Visualizer KIRTI 

## Command to compile costun-collection classes
- mvn test-compile
- java src\test\java\com\dsa\collections\linear\JArrayListTest

## Commands to run backend
- mvn clean install
- mvn spring-boot:run -pl dsa-backend


dsa-frontend/
├── package.json
├── vite.config.js
├── tailwind.config.js
├── postcss.config.js
├── index.html
├── .gitignore
│
├── public/
│   └── favicon.svg
│
└── src/
    ├── main.jsx                     # React root mount
    ├── App.jsx                      # top-level router: Home <-> a structure's workspace
    │
    ├── pages/
    │   └── Home.jsx                 # landing page: buttons for Array, LinkedList, Stack, Tree...
    │
    ├── shared/                      # reusable across every future data-structure module
    │   ├── api/
    │   │   └── client.js            # generic POST wrapper, e.g. executeOperations(endpoint, body)
    │   ├── components/
    │   │   ├── PlaybackControls.jsx # Play/Pause/Next/Prev + speed slider (stepType-agnostic)
    │   │   ├── OperationQueueList.jsx # renders queue[], handles remove (✕)
    │   │   ├── BackExecuteBar.jsx
    │   │   └── Button.jsx
    │   ├── hooks/
    │   │   └── useStepPlayer.js     # generic: given steps[], currentStep, isPlaying, speed -> advances via setInterval
    │   └── constants/
    │       └── stepTypes.js         # shared StepType string constants, mirrors your Java enum
    │
    └── modules/
        │
        ├── arraylist/                       # <-- your JArrayList animation module, fully self-contained
        │   ├── ArrayListWorkspace.jsx        # top component wiring queue+trace+playback together
        │   ├── components/
        │   │   ├── OutputWindow.jsx          # renders snapshot[] as boxes, applies per-step animation
        │   │   ├── ArrayBox.jsx              # single animated box (Framer Motion layout + variants)
        │   │   ├── OperationPicker.jsx       # grid of add/remove/set/get/sort/search buttons
        │   │   └── ArgumentForm.jsx          # dynamic form: 0/1/2 fields based on chosen op
        │   ├── animations/
        │   │   └── arrayStepVariants.js      # maps StepType -> Framer Motion variant (ADD/REMOVE/SET/GET/COMPARE/SHIFT/SWAP/FOUND/NOT_FOUND/SORT)
        │   ├── config/
        │   │   └── operationsConfig.js       # { name, label, argSchema } for each op — drives OperationPicker + ArgumentForm
        │   └── api/
        │       └── arrayListApi.js           # calls shared client with endpoint '/api/arraylist/execute'
        │
        ├── linkedlist/                       # empty placeholder for now, same shape as arraylist/
        │   └── .gitkeep
        │
        ├── stack/
        │   └── .gitkeep
        │
        └── tree/
            └── .gitkeep