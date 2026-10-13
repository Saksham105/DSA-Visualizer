import { AnimatePresence } from 'framer-motion'
import NodeBox from './NodeBox.jsx'
import NullNode from './NullNode.jsx'
import NodeConnector from './NodeConnector.jsx'
import { getStepStyle, isHighlighted } from '../animations/linkedListStepVariants.js'

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

      <div className="relative flex min-h-[300px] flex-wrap items-center gap-0 overflow-x-auto p-10">
        {snapshot.length === 0 ? (
          <p className="font-mono text-sm text-mist-400">
            empty — queue an operation and hit Execute to begin
          </p>
        ) : (
          <AnimatePresence mode="popLayout">
            <div key="chain" className="flex items-center">
              <NullNode key="null-head" />
              <NodeConnector key="conn-head" />
              {snapshot.map((value, index) => (
                <div key={index} className="flex items-center">
                  <NodeBox
                    index={index}
                    value={value}
                    active={step ? isHighlighted(index, step.highlightedIndices) : false}
                    colorToken={style?.color}
                  />
                  <NodeConnector />
                </div>
              ))}
              <NullNode key="null-tail" />
            </div>
          </AnimatePresence>
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
            {step.helpers && Object.keys(step.helpers).length > 0 && (
              <span className="ml-auto flex flex-shrink-0 gap-2">
                {Object.entries(step.helpers).map(([key, value]) => (
                  <span
                    key={key}
                    className="rounded border border-graphite-500 bg-graphite-800 px-1.5 py-0.5 font-mono text-[11px] text-mist-300"
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
