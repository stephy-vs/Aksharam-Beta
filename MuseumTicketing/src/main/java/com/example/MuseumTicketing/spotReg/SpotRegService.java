package com.example.MuseumTicketing.spotReg;

import com.example.MuseumTicketing.Guide.util.AlphaNumeric;
import com.example.MuseumTicketing.spotReg.category.additionCharge.AdditionChargeRepo;
import com.example.MuseumTicketing.spotReg.category.gst.GSTRepo;
import com.example.MuseumTicketing.spotReg.category.paymentMode.PaymentMode;
import com.example.MuseumTicketing.spotReg.category.paymentMode.PaymentModeRepo;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatus;
import com.example.MuseumTicketing.spotReg.category.paymentStatus.PaymentStatusRepo;
import com.example.MuseumTicketing.spotReg.category.price.PriceDataRepo;
import com.example.MuseumTicketing.spotReg.category.type.TypeRepo;
import com.example.MuseumTicketing.spotReg.userData.Institution.InstitutionData;
import com.example.MuseumTicketing.spotReg.userData.Institution.InstitutionDataRepo;
import com.example.MuseumTicketing.spotReg.userData.foreigner.ForeignerData;
import com.example.MuseumTicketing.spotReg.userData.foreigner.ForeignerDataRepo;
import com.example.MuseumTicketing.spotReg.userData.publicUser.PublicData;
import com.example.MuseumTicketing.spotReg.userData.publicUser.PublicRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpotRegService {
    private static final Logger log = LoggerFactory.getLogger(SpotRegService.class);
    @Autowired
    private PublicRepo publicRepo;
    @Autowired
    private InstitutionDataRepo institutionDataRepo;
    @Autowired
    private ForeignerDataRepo foreignerDataRepo;
    @Autowired
    private PriceDataRepo priceDataRepo;
    @Autowired
    private GSTRepo gstRepo;
    @Autowired
    private AdditionChargeRepo additionChargeRepo;
    @Autowired
    private PaymentModeRepo paymentModeRepo;
    @Autowired
    private PaymentStatusRepo paymentStatusRepo;
    @Autowired
    private AlphaNumeric alphaNumeric;
    @Autowired
    private TypeRepo typeRepo;
    @Autowired
    private AmountCalculation amountCalculation;


    public ResponseEntity<?> publicUserReg(SpotUserDto spotUserDto, Integer category) {
        PublicData publicDetails = new PublicData();
        publicDetails.setName(spotUserDto.getName());
        publicDetails.setPhNumber(spotUserDto.getPhNumber());
        publicDetails.setAdult(spotUserDto.getAdult());
        publicDetails.setChild(spotUserDto.getChild());
        publicDetails.setSeniorCitizen(spotUserDto.getSeniorCitizen());
        publicDetails.setVisitDate(spotUserDto.getVisitDate());
        Double totalAdultCharge=0.0;    Double totalChildCharge=0.0;    Double totalSeniorCitizenCharge=0.0;
        Double grandTotal;
        Integer userCount,typeId;
        if (spotUserDto.getAdult()>0){
            typeId = spotUserDto.getAdultTypeId();
            userCount = spotUserDto.getAdult();
            totalAdultCharge =amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        if (spotUserDto.getChild()>0){
            typeId=spotUserDto.getChildTypeId();
            userCount = spotUserDto.getChild();
            totalChildCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        if (spotUserDto.getSeniorCitizen()>0){
            typeId = spotUserDto.getSeniorCitizenTypeId();
            userCount = spotUserDto.getSeniorCitizen();
            totalSeniorCitizenCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        Double totalCharges = totalAdultCharge+totalChildCharge+totalSeniorCitizenCharge;
        Double totalGstRate =0.0; Double totalUserGst;
        totalGstRate = amountCalculation.CalculateGST();
        totalUserGst = totalGstRate*totalCharges;
        Integer extraCharge= amountCalculation.calculateAdditionalCharges();
        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
        publicDetails.setTotalAmount(totalCharges);
        publicDetails.setTotalGstCharge(totalUserGst);
        publicDetails.setTotalAdditionalCharges(extraCharge);
        publicDetails.setGrandTotal(grandTotal);
        publicDetails.setPaymentMode(spotUserDto.getPaymentMode());
        publicDetails.setPaymentStatusId(spotUserDto.getPaymentStatusId());
        Optional<PaymentStatus>paymentStatusOptional=paymentStatusRepo.findById(spotUserDto.getPaymentStatusId());
        Optional<PaymentMode>paymentModeOptional=paymentModeRepo.findById(spotUserDto.getPaymentMode());
        if (paymentModeOptional.isPresent() && paymentStatusOptional.isPresent()){
            PaymentStatus paymentStatus =paymentStatusOptional.get();
            String name =paymentStatus.getStatusName();
            PaymentMode paymentMode = paymentModeOptional.get();
            String modeName = paymentMode.getPaymentType();
            if ("cash".equalsIgnoreCase(modeName) && "received".equalsIgnoreCase(name)){
                publicDetails.setTicketId(alphaNumeric.generateSpotRandomNumber());
            }else {
                publicDetails.setTicketId(null);
            }
        }
        //publicDetails.setEmployeeId();
        publicRepo.save(publicDetails);
        return new ResponseEntity<>(publicDetails,HttpStatus.OK);
    }

    public ResponseEntity<?> institutionReg(SpotUserDto spotUserDto, Integer category) {
        InstitutionData institutionData = new InstitutionData();
        institutionData.setName(spotUserDto.getName());
        institutionData.setPhNumber(spotUserDto.getPhNumber());
        institutionData.setDistrict(spotUserDto.getDistrict());
        institutionData.setStudent(spotUserDto.getStudent());
        institutionData.setTeacher(spotUserDto.getTeacher());
        institutionData.setVisitDate(spotUserDto.getVisitDate());
        Double totalTeacherCharge=0.0;  Double totalStudentCharge=0.0;
        Double grandTotal;Integer userCount,typeId;
        if (spotUserDto.getTeacher()>0){
            typeId = spotUserDto.getTeacherTypeId();
            userCount = spotUserDto.getTeacher();
            totalTeacherCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        if (spotUserDto.getStudent()>0){
            typeId=spotUserDto.getStudentTypeId();
            userCount=spotUserDto.getStudent();
            totalStudentCharge = amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        Double totalCharges = totalStudentCharge+totalTeacherCharge;
        Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
        totalUserGst = totalGstRate*totalCharges;
        Integer extraCharge= amountCalculation.calculateAdditionalCharges();
        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
        institutionData.setTotalAmount(totalCharges);
        institutionData.setPaymentMode(spotUserDto.getPaymentMode());
        institutionData.setTotalGstCharge(totalUserGst);
        institutionData.setTotalAdditionalCharges(extraCharge);
        institutionData.setGrandTotal(grandTotal);
        institutionData.setPaymentMode(spotUserDto.getPaymentMode());
        institutionData.setPaymentStatusId(spotUserDto.getPaymentStatusId());
        Optional<PaymentStatus>paymentStatusOptional=paymentStatusRepo.findById(spotUserDto.getPaymentStatusId());
        Optional<PaymentMode>paymentModeOptional=paymentModeRepo.findById(spotUserDto.getPaymentMode());
        if (paymentModeOptional.isPresent() && paymentStatusOptional.isPresent()){
            PaymentStatus paymentStatus =paymentStatusOptional.get();
            String name =paymentStatus.getStatusName();
            PaymentMode paymentMode = paymentModeOptional.get();
            String modeName = paymentMode.getPaymentType();
            if ("cash".equalsIgnoreCase(modeName) && "received".equalsIgnoreCase(name)){
                institutionData.setTicketId(alphaNumeric.generateSpotRandomNumber());
            }else {
                institutionData.setTicketId(null);
            }
        }
        institutionDataRepo.save(institutionData);
        return new ResponseEntity<>(institutionData,HttpStatus.OK);
    }

    public ResponseEntity<?> ForeignerReg(SpotUserDto spotUserDto, Integer category) {
        ForeignerData foreignerData = new ForeignerData();
        foreignerData.setName(spotUserDto.getName());
        foreignerData.setPhNumber(spotUserDto.getPhNumber());
        foreignerData.setAdult(spotUserDto.getAdult());
        foreignerData.setChild(spotUserDto.getChild());
        foreignerData.setVisitDate(spotUserDto.getVisitDate());
        Double totalAdultCharge=0.0;    Double totalChildCharge=0.0;    Double grandTotal;
        Integer userCount,typeId;
        if (spotUserDto.getAdult()>0){
            typeId=spotUserDto.getAdultTypeId();
            userCount = spotUserDto.getAdult();
            totalAdultCharge=amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        if (spotUserDto.getChild()>0){
            typeId=spotUserDto.getChildTypeId();
            userCount = spotUserDto.getChild();
            totalChildCharge=amountCalculation.calculateTotalUserCharge(category,typeId,userCount);
        }
        Double totalCharges=totalAdultCharge+totalChildCharge;
        Double totalGstRate =amountCalculation.CalculateGST(),totalUserGst;
        totalUserGst = totalGstRate*totalCharges;
        Integer extraCharge= amountCalculation.calculateAdditionalCharges();
        grandTotal = amountCalculation.calculateGrandTotal(totalUserGst,extraCharge,totalCharges);
        foreignerData.setTotalAmount(totalCharges);
        foreignerData.setTotalGstCharge(totalUserGst);
        foreignerData.setTotalAdditionalCharges(extraCharge);
        foreignerData.setGrandTotal(grandTotal);
        foreignerData.setPaymentMode(spotUserDto.getPaymentMode());
        foreignerData.setPaymentStatusId(spotUserDto.getPaymentStatusId());
        Optional<PaymentStatus>paymentStatusOptional=paymentStatusRepo.findById(spotUserDto.getPaymentStatusId());
        Optional<PaymentMode>paymentModeOptional=paymentModeRepo.findById(spotUserDto.getPaymentMode());
        if (paymentModeOptional.isPresent() && paymentStatusOptional.isPresent()){
            PaymentStatus paymentStatus =paymentStatusOptional.get();
            String name =paymentStatus.getStatusName();
            PaymentMode paymentMode = paymentModeOptional.get();
            String modeName = paymentMode.getPaymentType();
            if ("cash".equalsIgnoreCase(modeName) && "received".equalsIgnoreCase(name)){
                foreignerData.setTicketId(alphaNumeric.generateSpotRandomNumber());
            }else {
                foreignerData.setTicketId(null);
            }
        }
        foreignerDataRepo.save(foreignerData);
        return new ResponseEntity<>(foreignerData,HttpStatus.OK);
    }

    public ResponseEntity<List<SpotUserDto>> getAllUserDetails() {
        List<SpotUserDto> spotUserDtoList = new ArrayList<>();
        List<PublicData> publicDataList = publicRepo.findAll();
        for (PublicData publicData : publicDataList){
            SpotUserDto spotUserDto = new SpotUserDto();
            spotUserDto.setName(publicData.getName());
            spotUserDto.setPhNumber(publicData.getPhNumber());
            spotUserDto.setAdult(publicData.getAdult());
            spotUserDto.setChild(publicData.getChild());
            spotUserDto.setSeniorCitizen(publicData.getSeniorCitizen());
            spotUserDto.setVisitDate(publicData.getVisitDate());
            spotUserDto.setGrandTotal(publicData.getTotalAmount());
//            spotUserDto.setPaymentMode(publicData.getPaymentMode());

            spotUserDtoList.add(spotUserDto);
        }

        List<InstitutionData>institutionDataList = institutionDataRepo.findAll();
        for (InstitutionData institutionData:institutionDataList){
            SpotUserDto spotUserDtoInstitution = new SpotUserDto();
            spotUserDtoInstitution.setName(institutionData.getName());
            spotUserDtoInstitution.setPhNumber(institutionData.getPhNumber());
            spotUserDtoInstitution.setDistrict(institutionData.getDistrict());
            spotUserDtoInstitution.setTeacher(institutionData.getTeacher());
            spotUserDtoInstitution.setStudent(institutionData.getStudent());
            spotUserDtoInstitution.setVisitDate(institutionData.getVisitDate());
            spotUserDtoInstitution.setGrandTotal(institutionData.getTotalAmount());
            spotUserDtoInstitution.setPaymentMode(institutionData.getPaymentMode());

            spotUserDtoList.add(spotUserDtoInstitution);
        }

        List<ForeignerData>foreignerDataList=foreignerDataRepo.findAll();
        for (ForeignerData foreignerData:foreignerDataList){
            SpotUserDto spotUserDtoForeigner = new SpotUserDto();
            spotUserDtoForeigner.setName(foreignerData.getName());
            spotUserDtoForeigner.setPhNumber(foreignerData.getPhNumber());
            spotUserDtoForeigner.setAdult(foreignerData.getAdult());
            spotUserDtoForeigner.setChild(foreignerData.getChild());
            spotUserDtoForeigner.setVisitDate(foreignerData.getVisitDate());
            spotUserDtoForeigner.setGrandTotal(foreignerData.getTotalAmount());
            spotUserDtoForeigner.setPaymentMode(foreignerData.getPaymentMode());

            spotUserDtoList.add(spotUserDtoForeigner);
        }
        return new ResponseEntity<>(spotUserDtoList,HttpStatus.OK);
    }

    public ResponseEntity<List<SpotUserDto>> getAllPublic() {
        List<SpotUserDto> spotUserDtoList = new ArrayList<>();
        List<PublicData> publicDataList = publicRepo.findAll();
        for (PublicData publicData : publicDataList){
            SpotUserDto spotUserDto = new SpotUserDto();
            spotUserDto.setName(publicData.getName());
            spotUserDto.setPhNumber(publicData.getPhNumber());
            spotUserDto.setAdult(publicData.getAdult());
            spotUserDto.setChild(publicData.getChild());
            spotUserDto.setSeniorCitizen(publicData.getSeniorCitizen());
            spotUserDto.setVisitDate(publicData.getVisitDate());
            spotUserDto.setGrandTotal(publicData.getTotalAmount());
//            spotUserDto.setPaymentMode(publicData.getPaymentMode());

            spotUserDtoList.add(spotUserDto);
        }
        return new ResponseEntity<>(spotUserDtoList,HttpStatus.OK);
    }

    public ResponseEntity<List<SpotUserDto>> getAllInstitution() {
        List<SpotUserDto> spotUserDtoList = new ArrayList<>();
        List<InstitutionData>institutionDataList = institutionDataRepo.findAll();
        for (InstitutionData institutionData:institutionDataList){
            SpotUserDto spotUserDtoInstitution = new SpotUserDto();
            spotUserDtoInstitution.setName(institutionData.getName());
            spotUserDtoInstitution.setPhNumber(institutionData.getPhNumber());
            spotUserDtoInstitution.setDistrict(institutionData.getDistrict());
            spotUserDtoInstitution.setTeacher(institutionData.getTeacher());
            spotUserDtoInstitution.setStudent(institutionData.getStudent());
            spotUserDtoInstitution.setVisitDate(institutionData.getVisitDate());
            spotUserDtoInstitution.setGrandTotal(institutionData.getTotalAmount());
            spotUserDtoInstitution.setPaymentMode(institutionData.getPaymentMode());

            spotUserDtoList.add(spotUserDtoInstitution);
        }
        return new ResponseEntity<>(spotUserDtoList,HttpStatus.OK);
    }

    public ResponseEntity<List<SpotUserDto>> getAllForeigner() {
        List<SpotUserDto> spotUserDtoList = new ArrayList<>();
        List<ForeignerData>foreignerDataList=foreignerDataRepo.findAll();
        for (ForeignerData foreignerData:foreignerDataList){
            SpotUserDto spotUserDtoForeigner = new SpotUserDto();
            spotUserDtoForeigner.setName(foreignerData.getName());
            spotUserDtoForeigner.setPhNumber(foreignerData.getPhNumber());
            spotUserDtoForeigner.setAdult(foreignerData.getAdult());
            spotUserDtoForeigner.setChild(foreignerData.getChild());
            spotUserDtoForeigner.setVisitDate(foreignerData.getVisitDate());
            spotUserDtoForeigner.setGrandTotal(foreignerData.getTotalAmount());
            spotUserDtoForeigner.setPaymentMode(foreignerData.getPaymentMode());

            spotUserDtoList.add(spotUserDtoForeigner);
        }
        return new ResponseEntity<>(spotUserDtoList,HttpStatus.OK);
    }
}
