// Each entry drives both the OperationPicker button grid and the
// ArgumentForm's dynamic fields. `args` describes what the form should ask
// for; `arguments` sent to the backend are always numbers, in this order.
export const arrayOperations = [
  { name: 'add', label: 'Add Last', args: [{ key: 'value', label: 'Value' }] },
  { name: 'addFirst', label: 'Add First', args: [{ key: 'value', label: 'Value' }] },
  {
    name: 'set',
    label: 'Update Using Index',
    args: [
      { key: 'index', label: 'Index' },
      { key: 'value', label: 'Value' },
    ],
  },
  { name: 'get', label: 'Get Value', args: [{ key: 'index', label: 'Index' }] },
  { name: 'remove', label: 'Delete', args: [{ key: 'index', label: 'Index' }] },
  { name: 'linearSearch', label: 'Linear Search', args: [{ key: 'target', label: 'Target' }] },
  { name: 'binarySearch', label: 'Binary Search', args: [{ key: 'target', label: 'Target' }] },
  { name: 'bubbleSort', label: 'Bubble Sort', args: [] },
  { name: 'insertionSort', label: 'Insertion Sort', args: [] },
]

export function findOperation(name) {
  return arrayOperations.find((op) => op.name === name)
}
