package ssafy.autonomous.pathfinder.domain.facility.domain

import javax.persistence.*

@Entity
class RoomMap(

        // Room 중앙 위치
        val longitude: Double,
        val latitude: Double,


        // 입구 좌상, 우상, 좌하, 우하 추가
        val entrancLeftUp: Double ?= 0.0,
        val entranceRightUp: Double ?= 0.0,
        val entranceLeftDown: Double ?= 0.0,
        val entranceRightDown: Double ?= 0.0,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "room_map_id")
        val id: Int,


        // 다대일 단방향
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "floors_id")
        var floors: Floors ?= null,

        // 일대일 상하좌우 방향
//        @OneToOne(fetch = FetchType.LAZY, mappedBy="roomMap", cascade = [CascadeType.ALL])
//        var direction: Direction ?= null,

        // 일대일 weight
        @OneToOne(fetch = FetchType.LAZY, mappedBy="roomMap", cascade = [CascadeType.ALL])
        var weight: Weight ?= null
)