import { useState } from 'react'
import { motion, AnimatePresence } from 'framer-motion'

const STRUCTURES = [
  { id: 'arraylist', label: 'Array', number: 1, ready: true },
  { id: 'linkedlist', label: 'Linked List', number: 2, ready: true },
  { id: 'stack', label: 'Stack', number: 3, ready: true },
  { id: 'queue', label: 'Queue', number: 4, ready: true },
  { id: 'hashset', label: 'Hashing', number: 5, ready: true },
  { id: 'treemap', label: 'Tree', number: 6, ready: true },
]

export default function Home({ onSelect }) {
  const [showInfo, setShowInfo] = useState(false)

  return (
    <div className="mx-auto flex max-w-6xl flex-col gap-10 px-4 py-16 sm:px-8">
      <header className="flex flex-col gap-4">
        {/* App name is the clear visual anchor of the page */}
        <h1 className="font-mono text-5xl font-extrabold leading-none tracking-tight text-mist-100 sm:text-7xl">
          DSA-VISUALIZER
        </h1>

        <p className="font-mono text-xs uppercase tracking-widest text-cell-success">
          - imagination to reality
        </p>

        <button
          onClick={() => setShowInfo((v) => !v)}
          className="mt-2 flex w-fit items-center gap-2 rounded-md border border-graphite-600
            bg-graphite-800 px-3 py-1.5 font-mono text-xs text-mist-300 transition-colors
            hover:border-cell-shift/60 hover:text-mist-100"
          aria-expanded={showInfo}
        >
          <span
            className={`inline-flex h-4 w-4 items-center justify-center rounded-full border
              text-[10px] font-bold ${showInfo ? 'border-cell-shift text-cell-shift' : 'border-mist-400 text-mist-400'}`}
          >
            i
          </span>
          {showInfo ? 'Hide info' : 'Info'}
        </button>

        <AnimatePresence>
          {showInfo && (
            <motion.p
              initial={{ opacity: 0, height: 0 }}
              animate={{ opacity: 1, height: 'auto' }}
              exit={{ opacity: 0, height: 0 }}
              transition={{ duration: 0.2 }}
              className="max-w-2xl overflow-hidden font-mono text-sm leading-relaxed text-mist-300"
            >
              Pick a structure below, queue up the operations you want to run, and
              the traced execution engine plays back every comparison, shift, and
              swap as an animation.
            </motion.p>
          )}
        </AnimatePresence>
      </header>

      <section>
        <p className="mb-3 font-mono text-[11px] uppercase tracking-wide text-mist-400">
          Data Structures
        </p>
        <div className="grid grid-cols-2 gap-3 sm:grid-cols-3 md:grid-cols-4">
          {STRUCTURES.map((s, i) => (
            <motion.button
              key={s.id}
              disabled={!s.ready}
              onClick={() => s.ready && onSelect(s.id)}
              initial={{ opacity: 0, y: 8 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: i * 0.05, duration: 0.3 }}
              whileHover={s.ready ? { y: -2 } : undefined}
              className={`group relative flex flex-col items-start gap-3 rounded-lg border p-4 text-left
                transition-colors
                ${
                  s.ready
                    ? 'border-graphite-600 bg-graphite-800 hover:border-cell-success/60 cursor-pointer'
                    : 'border-graphite-700 bg-graphite-800/40 cursor-not-allowed'
                }`}
            >
              <span className="font-mono text-[11px] text-mist-400">{s.number}</span>
              <span
                className={`font-mono text-base font-semibold ${
                  s.ready ? 'text-mist-100' : 'text-mist-400'
                }`}
              >
                {s.label}
              </span>
              {!s.ready && (
                <span className="font-mono text-[10px] uppercase tracking-wide text-mist-400">
                  coming soon
                </span>
              )}
              {s.ready && (
                <span className="absolute right-3 top-3 h-1.5 w-1.5 rounded-full bg-cell-success" />
              )}
            </motion.button>
          ))}
        </div>
      </section>
    </div>
  )
}
