(ns particles.sketches.fire
  (:require
    [quil.core :as q]
    [quil.middleware :as m]
    [particles.fire :as fire]))

(defn run []
  (q/defsketch fire
    :title "Fire"
    :size [750 750]
    :setup fire/setup
    :update fire/step
    :draw fire/draw
    :renderer :opengl
    :features [:no-safe-fns :keep-on-top :exit-on-close]
    :middleware [m/fun-mode]))
