package com.breno.gitviewer.helper

import android.util.Log
import com.breno.gitviewer.R

class HelperEvents {

    companion object {
        fun returnColor(language: String) : Int {
            return when (language) {
                "Kotlin" -> R.color.lilas
                "Shell" -> R.color.black
                "Emacs Lisp" -> R.color.gray_light
                "Python" -> R.color.orange
                "Vim script" -> R.color.black
                "HTML" -> R.color.blue
                "Ruby" -> R.color.red
                "JavaScript" -> R.color.green
                "Java" -> R.color.lilas
                "" -> R.color.gray_dark
                else -> R.color.black
            }
        }
    }

    fun verifyEvent(type: String): ModelEventAdapter {

       return when(type) {
            "PushEvent" -> {
                ModelEventAdapter(R.color.blue, R.drawable.ic_up, R.string.push)
            }
            "WatchEvent" -> {
                ModelEventAdapter(R.color.red, R.drawable.ic_eyes, R.string.watch)
            }
            "CreateEvent" -> {
                ModelEventAdapter(R.color.green, R.drawable.ic_add, R.string.create)
            }
            "IssuesEvent" -> {
                ModelEventAdapter(R.color.red, R.drawable.ic_problem, R.string.problems)
            }
            "ForkEvent" -> {
                ModelEventAdapter(R.color.yellow, R.drawable.ic_fork, R.string.fork)
            }
            "IssueCommentEvent" -> {
                ModelEventAdapter(R.color.gray_light, R.drawable.ic_comment, R.string.commentProblem)
            }
            "PullRequestEvent" -> {
                ModelEventAdapter(R.color.green, R.drawable.ic_pull, R.string.pull)
            }
            "PullRequestReviewCommentEvent" -> {
                ModelEventAdapter(R.color.orange, R.drawable.ic_comment, R.string.review_comment)
            }
            "PullRequestReviewEvent" -> {
                ModelEventAdapter(R.color.yellow, R.drawable.ic_review, R.string.review)
            }
           "DeleteEvent" -> {
                ModelEventAdapter(R.color.red, R.drawable.ic_delete, R.string.delete)
            }
            else -> {
                Log.e("BRENOL", type)
                ModelEventAdapter(R.color.gray, R.drawable.ic_circle, R.string.nothing)
            }
        }
    }

}

class ModelEventAdapter(val color: Int, val image: Int, val message: Int)
