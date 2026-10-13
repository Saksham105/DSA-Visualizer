// Mirrors JLinkedListController's switch cases exactly.
export const linkedListOperations = [
  { name: 'add', label: 'Add Last', args: [{ key: 'value', label: 'Value' }] },
  { name: 'addFirst', label: 'Add First', args: [{ key: 'value', label: 'Value' }] },
  {
    name: 'set',
    label: 'Update Using Position',
    args: [
      { key: 'index', label: 'Position' },
      { key: 'value', label: 'Value' },
    ],
  },
  { name: 'get', label: 'Get Node', args: [{ key: 'index', label: 'Index' }] },
  { name: 'remove', label: 'Delete Node', args: [{ key: 'index', label: 'Index' }] },
  { name: 'linearSearch', label: 'Linear Search', args: [{ key: 'target', label: 'Target' }] },
  { name: 'reverse', label: 'Reverse Linklist', args: [] },
  { name: 'insertionSort', label: 'Insertion Sort', args: [] },
  { name: 'middleNode', label: 'Middle Node', args: [] },
]
