package com.apollographql.scalajs

import com.apollographql.scalajs.react.Query
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._
import slinky.core.facade.Fragment
import slinky.core.facade.Hooks._
import slinky.core.SyntheticEvent
import org.scalajs.dom.{html, Event}

@react class TodosView extends StatelessComponent {
  type Props = Unit

  def render(): ReactElement = {
    Fragment(
      h1("Todos"),
      Query(AllTodosQuery) { res =>
        res.data.map { d =>
          div(
            d.todos.toList.flatMap { todos =>
              todos.toList.flatMap { todo =>
                todo.map { todo =>
                  div(key := todo.id.toString)(
                    h1(todo.typ),
                  )
                }
              }
            }
          ): ReactElement
        }.getOrElse(h1("loading!"))
      },
      AddTodo()
    )
  }
}
