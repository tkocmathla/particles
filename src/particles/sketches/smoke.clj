(ns particles.sketches.smoke
  (:require
    [quil.core :as q]
    [quil.middleware :as m]
    [particles.smoke :as smoke]))

(defn run []
  (q/defsketch smoke
    :title "Smoke"
    :size [750 750]
    :setup smoke/setup
    :update smoke/step
    :draw smoke/draw
    :renderer :opengl
    :features [:no-safe-fns :keep-on-top :exit-on-close]
    :middleware [m/fun-mode]))
