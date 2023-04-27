package ssafy.autonomous.passfinder.administrator.domain

import javax.persistence.*

@Entity
class Manager(


        val managerName:String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "manager_id")
        val id: Long ?= null,

        @OneToOne(mappedBy = "manager")
        var building: Building
)