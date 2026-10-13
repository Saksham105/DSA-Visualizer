import { AnimatePresence } from 'framer-motion'
import ArrayBox from './ArrayBox.jsx'
import { getStepStyle, isHighlighted } from '../animations/arrayStepVariants.js'

// Static class lookup — Tailwind's JIT can't see dynamically interpolated
// class names like `text-${var}`, so badge colors must be listed literally.
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

  return (
    <div className="relative overflow-hidden rounded-xl border border-graphite-600 bg-graphite-800/40">
      <div className="absolute inset-0 bg-grid-paper pointer-events-none" />

      {/* Boxes */}
      <div className="relative flex min-h-[300px] flex-wrap items-center gap-5 p-10">
        {snapshot.length === 0 && (
          <p className="font-mono text-sm text-mist-400">
            empty — queue an operation and hit Execute to begin
          </p>
        )}
        <AnimatePresence mode="popLayout">
          {snapshot.map((value, index) => (
            <ArrayBox
              key={index}
              index={index}
              value={value}
              active={step ? isHighlighted(index, step.highlightedIndices) : false}
              colorToken={style?.color}
            />
          ))}
        </AnimatePresence>
      </div>

      {/* Console strip: step badge + description, terminal style */}
      <div className="relative flex items-center gap-3 border-t border-graphite-600 bg-graphite-900/70 px-4 py-2.5">
        <span className="inline-block h-2 w-2 flex-shrink-0 animate-pulse rounded-full bg-cell-success" />
        {step ? (
          <>
            <span
              className={`rounded px-1.5 py-0.5 font-mono text-[30px] font-bold tracking-wide
                ${style.ring ? BADGE_TEXT_CLASSES[style.color] : 'text-mist-500'}`}
            >
              {getStepStyle(step.stepType).badge}
            </span>
            <span className="truncate font-mono text-xl text-mist-500">{step.description}</span>
            {step.helpers && Object.keys(step.helpers).length > 0 && (
              <span className="ml-auto flex flex-shrink-0 gap-2">
                {Object.entries(step.helpers).map(([key, value]) => (
                  <span
                    key={key}
                    className="rounded border border-graphite-500 bg-graphite-800 px-1.5 py-0.5
                      font-mono text-[11px] text-mist-500"
                    title={`${key} register`}
                  >
                    {key}: <span className="text-cell-swap">{value}</span>
                  </span>
                ))}
              </span>
            )}
          </>
        ) : (
          <span className="font-mono text-xs text-mist-400">awaiting execution…</span>
        )}
      </div>
    </div>
  )
}
