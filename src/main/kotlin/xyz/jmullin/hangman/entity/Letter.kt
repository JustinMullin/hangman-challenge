package xyz.jmullin.hangman.entity

import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.rendering.RenderStage
import xyz.jmullin.hangman.Stage

class Letter : Entity2D() {
    override fun render(stage: RenderStage) {
        stage.draw(Stage.Ui) {

        }

        super.render(stage)
    }
}