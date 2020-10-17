package com.apollographql.scalajs

import com.apollographql.scalajs.react.Query
import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade._
import slinky.web.html._
import slinky.core.facade.Hooks._
import org.scalajs.dom.{html, Event}
import com.apollographql.scalajs.react.Mutation
import com.apollographql.scalajs.react.UpdateStrategy

@react object AddTodo {
  type Props = Unit

  val component = FunctionalComponent[Props] { props =>
    val (todoText, setTodoText) = useState("")

    Mutation(AddTodoMutation, UpdateStrategy(Seq("AllTodos"))) { (todoMut, res) =>
      div(
        input(value := todoText, onChange := ((e) => setTodoText(e.target.value))),
        button(
          if (res.loading) "Adding" else "Add Todo", 
          onClick := ((_) => {
            todoMut(AddTodoMutation.Variables(todoText))
            setTodoText("")
          }),
          disabled := res.loading || todoText.trim.isEmpty)
      )
    }
  }
}
