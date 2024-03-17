package com.demo.flink.model

data class CustomerPayments(
  val id: String,
  val amount: Long,
  val count: Long,
  val location: String,
  val startTime: Long,
  val endTime: Long,
  val fraudulent: Boolean,
)
