import { AnimatePresence } from 'framer-motion'
import SlotBox from './SlotBox.jsx'
import { getStepStyle, isHighlighted } from '../animations/hashSetStepVariants.js'

// Static class lookup — Tailwind's JIT can't see dynamically interpolated
// class names like `text-${var}`, so badge colors must be listed literally.
const BADGE_TEXT_CLASSES = {
  'cell-success': 'text-cell-success',
  'cell-danger': 'text-cell-danger',
  'cell-shift': 'text-cell-shift',
  'cell-compare': 'text-cell-compare',
  'cell-swap': 'text-cell-swap',
}

export default function OutputWindow({ step, previousStep }) {
  const slots = step?.snapshot ?? []
  const style = step ? getStepStyle(step.stepType) : null

  const filled = slots.filter((v) => v !== 'null' && v !== 'DEL').length
  const deleted = slots.filter((v) => v === 'DEL').length
  const previousCapacity = previousStep ? previousStep.snapshot.length : slots.length
  const resized = step && previousCapacity !== slots.length

  return (
    <div className="relative overflow-hidden rounded-xl border border-graphite-600 bg-graphite-800/40">
      <div className="absolute inset-0 bg-grid-paper pointer-events-none" />

      <div className="relative flex min-h-[300px] flex-col gap-5 p-10">
        {slots.length === 0 ? (
          <p className="font-mono text-sm text-mist-400">
            empty — queue an operation and hit Execute to begin
          </p>
        ) : (
          <>
            <div className="flex flex-wrap gap-4 font-mono text-[11px] text-mist-400">
              <span>
                elements <span className="text-mist-100">{filled}</span>
              </span>
              <span>
                capacity <span className="text-mist-100">{slots.length}</span>
              </span>
              <span>
                deleted slots <span className="text-mist-100">{deleted}</span>
              </span>
              {resized && (
                <span className="text-cell-compare">
                  table grew from {previousCapacity} to {slots.length} slots — every position was recalculated
                </span>
              )}
            </div>

            <AnimatePresence mode="popLayout">
              <div className="flex flex-wrap gap-5">
                {slots.map((value, index) => (
                  <SlotBox
                    key={index}
                    index={index}
                    value={value}
                    active={step ? isHighlighted(index, step.highlightedIndices) : false}
                    colorToken={style?.color}
                  />
                ))}
              </div>
            </AnimatePresence>
          </>
        )}
      </div>

      {/* Console strip: step badge + description, terminal style */}
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
