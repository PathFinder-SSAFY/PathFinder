package ssafy.autonomous.passfinder.facility.domain

import javax.persistence.*

@Entity
class RoomMap(

        // 위도, 경도
        val longitude: Double,
        val latitude: Double,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "room_map_id")
        val id: Int,


        // 다대일 단방향
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "floors_id")
        var floors: Floors ?= null,

        // 일대일 상하좌우 방향
        @OneToOne(fetch = FetchType.LAZY, mappedBy="roomMap", cascade = [CascadeType.ALL])
        var direction: Direction ?= null,

        // 일대일 weight
        @OneToOne(fetch = FetchType.LAZY, mappedBy="roomMap", cascade = [CascadeType.ALL])
        var weight: Weight ?= null
)