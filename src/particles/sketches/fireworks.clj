(ns particles.sketches.fireworks
  (:require
    [quil.core :as q]
    [quil.middleware :as m]
    [particles.fireworks :as fireworks]))

(defn run []
  (q/defsketch fireworks
    :title "Fireworks"
    :size [750 750]
    :setup fireworks/setup
    :update fireworks/step
    :draw fireworks/draw
    :renderer :opengl
    :features [:no-safe-fns :keep-on-top :exit-on-close]
    :middleware [m/fun-mode]))
