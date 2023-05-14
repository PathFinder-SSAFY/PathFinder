package ssafy.autonomous.pathfinder.domain.floors.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ssafy.autonomous.pathfinder.domain.floors.dto.request.FloorsCurrentLocationRequestDto





@SpringBootTest
class FloorsServiceImplListTest @Autowired constructor(
    private var floorsService : FloorsService,
) {


    @Test
    @DisplayName("현재 위치가 남자 화장실 안일 때")
    fun 현재위치가_남자화장실_안(){
        // given
        val floorsCurrentLocation = FloorsCurrentLocationRequestDto(28.50, 0.0, -13.12)

        // when
        val curLocation = floorsService.getCurrentLocation(floorsCurrentLocation)

        // then
        Assertions.assertEquals(curLocation, "4층 남자 화장실 안")
    }


    @Test
    @DisplayName("현재 위치가 4층 남자 화장실 입구 앞")
    fun 현재위치가_남자화장실_입구_앞(){
        // given
        val floorsCurrentLocation = FloorsCurrentLocationRequestDto(27.90, 0.0, -10.2)

        // when
        val curLocation = floorsService.getCurrentLocation(floorsCurrentLocation)

        // then
        Assertions.assertEquals(curLocation, "4층 남자 화장실 입구 앞")
    }

    @Test
    @DisplayName("현재 위치가 강의실 입구")
    fun 현재위치가_강의실_입구(){
        // given
        val floorsCurrentLocation = FloorsCurrentLocationRequestDto(15.520, 0.0, -7.00)

        // when
        val curLocation = floorsService.getCurrentLocation(floorsCurrentLocation)

        // then
        Assertions.assertEquals(curLocation, "4층 대강의실 입구")
    }


    @Test
    @DisplayName("현재 위치가 휴게실 401 입구")
    fun 현재위치가_휴게실401_입구(){
        // given
        val floorsCurrentLocation = FloorsCurrentLocationRequestDto(21.80, 0.0, -5.8)

        // when
        val curLocation = floorsService.getCurrentLocation(floorsCurrentLocation)

        // then
        Assertions.assertEquals(curLocation, "휴게실 401 입구")
    }


    @Test
    @DisplayName("현재 좌표가 벽의 범위에 포함되는 좌표")
    fun 현재위치가_벽의_범위_포함되는좌표(){
        // given
        val floorsCurrentLocation = FloorsCurrentLocationRequestDto(38.12, 0.0, -8.00)

        // when
        val curLocation = floorsService.getCurrentLocation(floorsCurrentLocation)

        // then
        Assertions.assertEquals(curLocation, "현재 좌표는 벽의 범위에 포함되는 좌표입니다. 사용자 위치가 될 수 없습니다.")

    }


    @Test
    @DisplayName("층 범위를 벗어났을 때")
    fun 층_범위를_벗어났을_때(){
        // given
        val floorsCurrentLocation = FloorsCurrentLocationRequestDto(10.02, 0.0, 8.00)

        // when
        val curLocation = floorsService.getCurrentLocation(floorsCurrentLocation)

        // then
        Assertions.assertEquals(curLocation, "층의 범위를 벗어난 좌표입니다. 사용자 위치가 될 수 없습니다.")
    }

    @Test
    @DisplayName("4층 복도")
    fun 층_복도(){
        // given
        val floorsCurrentLocation = FloorsCurrentLocationRequestDto(24.24, 0.0, -7.0)

        // when
        val curLocation = floorsService.getCurrentLocation(floorsCurrentLocation)

        // then
        Assertions.assertEquals(curLocation, "4층 복도")
    }
}