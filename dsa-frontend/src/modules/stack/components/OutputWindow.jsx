import { AnimatePresence } from 'framer-motion'
import StackBox from './StackBox.jsx'
import { getStepStyle, isHighlighted } from '../animations/stackStepVariants.js'

const BADGE_TEXT_CLASSES = {
  'cell-success': 'text-cell-success',
  'cell-danger': 'text-cell-danger',
  'cell-shift': 'text-cell-shift',
  'cell-compare': 'text-cell-compare',
  'cell-swap': 'text-cell-swap',
}

export default function OutputWindow({ step }) {
  const snapshot = step?.snapshot ?? []
  const style = step ? getStepStyle(step.stepType) : null

  // Last element in the snapshot is the top of the stack. Reverse for
  // display so the top renders visually at the top and the stack grows
  // upward as elements are pushed, matching a real call-stack diagram.
  const displayOrder = [...snapshot].map((value, i) => ({ value, index: i })).reverse()

  return (
    <div className="relative overflow-hidden rounded-xl border border-graphite-600 bg-graphite-800/40">
      <div className="absolute inset-0 bg-grid-paper pointer-events-none" />

      <div className="relative flex min-h-[300px] flex-col items-center justify-end gap-2 p-10">
        {snapshot.length === 0 ? (
          <p className="font-mono text-sm text-mist-400">
            empty — queue an operation and hit Execute to begin
          </p>
        ) : (
          <>
            <AnimatePresence mode="popLayout">
              {displayOrder.map(({ value, index }, displayIndex) => (
                <StackBox
                  key={index}
                  index={index}
                  value={value}
                  isTop={displayIndex === 0}
                  active={step ? isHighlighted(index, step.highlightedIndices) : false}
                  colorToken={style?.color}
                />
              ))}
            </AnimatePresence>
            {/* floor line — grounds the stack visually */}
            <div className="mt-1 h-1 w-52 rounded-full bg-graphite-600" />
          </>
        )}
      </div>

      <div className="relative flex items-center gap-3 border-t border-graphite-600 bg-graphite-900/70 px-4 py-2.5">
        <span className="inline-block h-2 w-2 flex-shrink-0 animate-pulse rounded-full bg-cell-success" />
        {step ? (
          <>
            <span
              className={`rounded px-1.5 py-0.5 font-mono text-[30px] font-bold tracking-wide
                ${style.ring ? BADGE_TEXT_CLASSES[style.color] : 'text-mist-300'}`}
            >
              {getStepStyle(step.stepType).badge}
            </span>
            <span className="truncate font-mono text-xl text-mist-300">{step.description}</span>
          </>
        ) : (
          <span className="font-mono text-xs text-mist-400">awaiting execution…</span>
        )}
      </div>
    </div>
  )
}
