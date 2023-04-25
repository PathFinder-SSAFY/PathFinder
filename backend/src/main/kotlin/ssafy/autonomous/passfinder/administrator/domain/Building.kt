package ssafy.autonomous.passfinder.administrator.domain

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

class Building(


        val buildingName: String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="building_id")
        val id : Int

//        @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
//val orderItems: MutableList<OrderItem> = mutableListOf()
//
//        @OneToMany(mappedBy = "")
)