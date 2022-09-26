package com.breno.gitviewer.model

import android.util.Log
import com.breno.gitviewer.R

data class Event(
    val type: String,
    val actor: Actor
) {

    init {
        verifyEvent(type)
    }
    var color: Int = 0
    var image: Int = 0
    var message: Int = 0

    private fun verifyEvent(type: String) {

        return when(type) {
            "PushEvent" -> {
                fillInfoAdapter(R.color.blue, R.drawable.ic_up, R.string.push)
            }
            "WatchEvent" -> {
                fillInfoAdapter(R.color.red, R.drawable.ic_eyes, R.string.watch)
            }
            "CreateEvent" -> {
                fillInfoAdapter(R.color.green, R.drawable.ic_add, R.string.create)
            }
            "IssuesEvent" -> {
                fillInfoAdapter(R.color.red, R.drawable.ic_problem, R.string.problems)
            }
            "ForkEvent" -> {
                fillInfoAdapter(R.color.yellow, R.drawable.ic_fork, R.string.fork)
            }
            "IssueCommentEvent" -> {
                fillInfoAdapter(R.color.gray_light, R.drawable.ic_comment, R.string.commentProblem)
            }
            "PullRequestEvent" -> {
                fillInfoAdapter(R.color.green, R.drawable.ic_pull, R.string.pull)
            }
            "PullRequestReviewCommentEvent" -> {
                fillInfoAdapter(R.color.orange, R.drawable.ic_comment, R.string.review_comment)
            }
            "PullRequestReviewEvent" -> {
                fillInfoAdapter(R.color.yellow, R.drawable.ic_review, R.string.review)
            }
            "DeleteEvent" -> {
                fillInfoAdapter(R.color.red, R.drawable.ic_delete, R.string.delete)
            }
            else -> {
                Log.e("BRENOL", type)
                fillInfoAdapter(R.color.gray, R.drawable.ic_circle, R.string.nothing)
            }
        }
    }

    private fun fillInfoAdapter(color: Int, drawable: Int, message: Int) {
        this.color = color
        this.image = drawable
        this.message = message

    }
}