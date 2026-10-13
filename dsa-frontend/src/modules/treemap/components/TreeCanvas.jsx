import { useMemo, useState } from 'react'
import { motion } from 'framer-motion'
import { buildTree, layoutTree } from '../utils/layoutTree.js'

const NODE = 56
const MOVE = { type: 'spring', stiffness: 200, damping: 24 }

const COLOR_CLASSES = {
  'cell-success': '#34D399',
  'cell-danger': '#F87171',
  'cell-shift': '#60A5FA',
  'cell-compare': '#F0B429',
  'cell-swap': '#A78BFA',
}

export default function TreeCanvas({ step, colorToken }) {
  const entries = step?.snapshot ?? []
  const { nodes, edges, width, height } = useMemo(() => layoutTree(buildTree(entries)), [entries])
  const [selectedKey, setSelectedKey] = useState(null)

  const onPath = new Set((step?.highlightedIndices ?? []).filter((i) => i >= 0))
  const colors = COLOR_CLASSES[colorToken] ?? COLOR_CLASSES['cell-shift']

  if (nodes.length === 0) return <p className="font-mono text-sm text-mist-400">empty — queue an operation and hit Execute</p>

  return (
    <div className="w-full overflow-auto">
      <svg width={width} height={height} className="mx-auto block">
        {/* edges */}
        {edges.map((e, i) => {
          const lit = onPath.has(e.parentIndex) && onPath.has(e.childIndex)
          return (
            <motion.line
              key={i}
              initial={false}
              animate={{ x1: e.x1, y1: e.y1 + NODE / 4, x2: e.x2, y2: e.y2 - NODE / 4 }}
              transition={MOVE}
              stroke={lit ? colors : '#3A4557'}
              strokeWidth={lit ? 3 : 1.75}
              strokeLinecap="round"
            />
          )
        })}

        {/* nodes */}
        {nodes.map((n) => {
          const isOnPath = onPath.has(n.preIndex)
          const showValue = selectedKey === n.key
          return (
            <motion.g key={n.key} initial={false} animate={{ x: n.x - NODE / 2, y: n.y - NODE / 2 }} transition={MOVE}>
              <rect
                x={0}
                y={0}
                width={NODE}
                height={NODE}
                rx={4}
                className={`cursor-pointer transition-colors ${
                  isOnPath ? 'stroke-2' : 'stroke-[1.75px]'
                } fill-graphite-800`}
                stroke={isOnPath ? colors : '#3A4557'}
                onClick={() => setSelectedKey(showValue ? null : n.key)}
              />
              <text x={NODE / 2} y={NODE / 2} textAnchor="middle" dominantBaseline="central" fontSize="18" fontWeight="700" fill="white">
                {n.key}
              </text>
              {showValue && (
                <text x={NODE / 2} y={NODE + 20} textAnchor="middle" fontSize="16" fill="#FFFFFF">
                  {n.value}
                </text>
              )}
            </motion.g>
          )
        })}
      </svg>
    </div>
  )
}
