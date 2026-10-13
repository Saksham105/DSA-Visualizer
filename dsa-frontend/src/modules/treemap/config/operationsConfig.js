// Mirrors JTreeMapController's switch cases exactly. `insert`'s value is
// sent as a string — the backend's JTreeMap<Integer, String> stores it as
// one, even when it looks numeric.
export const treeMapOperations = [
  {
    name: 'insert',
    label: 'Insert Node',
    args: [
      { key: 'key', label: 'Key' },
      { key: 'value', label: 'Value', type: 'text', placeholder: 'text' },
    ],
  },
  { name: 'delete', label: 'Delete Node', args: [{ key: 'key', label: 'Key' }] },
  { name: 'search', label: 'Search Node', args: [{ key: 'key', label: 'Key' }] },
]

export function findOperation(name) {
  return treeMapOperations.find((op) => op.name === name)
}
