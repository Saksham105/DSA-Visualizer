import TreeCanvas from './TreeCanvas.jsx'
import { getStepStyle } from '../animations/treeMapStepVariants.js'

const BADGE_TEXT_CLASSES = {
  'cell-success': 'text-cell-success',
  'cell-danger': 'text-cell-danger',
  'cell-shift': 'text-cell-shift',
  'cell-compare': 'text-cell-compare',
  'cell-swap': 'text-cell-swap',
}

export default function OutputWindow({ step }) {
  const style = step ? getStepStyle(step.stepType) : null

  return (
    <div className="relative overflow-hidden rounded-xl border border-graphite-600 bg-graphite-800/40">
      <div className="absolute inset-0 bg-grid-paper pointer-events-none" />

      <div className="relative flex min-h-[300px] items-center justify-center overflow-x-auto p-10">
        <TreeCanvas step={step} colorToken={style?.color} />
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
