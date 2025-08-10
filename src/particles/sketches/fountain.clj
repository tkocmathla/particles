(ns particles.sketches.fountain
  (:require
    [quil.core :as q]
    [quil.middleware :as m]
    [particles.fountain :as fountain]))

(defn run []
  (q/defsketch fountain
    :title "Fountain"
    :size [750 750]
    :setup fountain/setup
    :update fountain/step
    :draw fountain/draw
    :renderer :opengl
    :features [:no-safe-fns :keep-on-top :exit-on-close]
    :middleware [m/fun-mode]))
