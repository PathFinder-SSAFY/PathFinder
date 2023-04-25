package ssafy.autonomous.passfinder.administrator.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Manager(


        val managerName:String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long ?= null




)