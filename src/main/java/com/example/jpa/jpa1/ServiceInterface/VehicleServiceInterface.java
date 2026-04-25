package com.example.jpa.jpa1.ServiceInterface;


import com.example.jpa.jpa1.Dto.ApiResponseDto;
import com.example.jpa.jpa1.Dto.VehicleDto;

public interface VehicleServiceInterface {
	
    public VehicleDto addData(VehicleDto vehicleDto) ;
    
    ApiResponseDto<?> showData(String name, int page);

    ApiResponseDto<VehicleDto> updateData(int id, VehicleDto dto);

    ApiResponseDto<String> deleteData(int id);

}
