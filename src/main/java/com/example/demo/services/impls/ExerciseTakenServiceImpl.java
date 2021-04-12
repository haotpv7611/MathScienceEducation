package com.example.demo.services.impls;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ExerciseTakenRequestDTO;
import com.example.demo.dtos.ExerciseTakenResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Account;
import com.example.demo.models.Exercise;
import com.example.demo.models.ExerciseTaken;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.IExerciseTakenRepository;
import com.example.demo.services.IExerciseTakenService;

@Service
public class ExerciseTakenServiceImpl implements IExerciseTakenService {
	Logger logger = LoggerFactory.getLogger(ExerciseTakenServiceImpl.class);

	private final String ACTIVE_STATUS = "ACTIVE";

	@Autowired
	IExerciseTakenRepository iExerciseTakenRepository;

	@Autowired
	IAccountRepository iAccountRepository;

	@Autowired
	IExerciseRepository iExerciseRepository;

	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public String findTakenObjectById(long id) {
		String takenObject = null;
		ExerciseTaken exerciseTaken = iExerciseTakenRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
		takenObject = exerciseTaken.getTakenObject();
		return takenObject;
	}

	@Override
	public List<ExerciseTakenResponseDTO> findAllByExerciseId(long exerciseId, long accountId) {
		List<ExerciseTakenResponseDTO> exerciseTakenResponseDTOList = new ArrayList<>();
		List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository.findByExerciseIdAndAccountId(exerciseId,
				accountId);

		if (!exerciseTakenList.isEmpty()) {
			for (ExerciseTaken exerciseTaken : exerciseTakenList) {
				long id = exerciseTaken.getId();
				float totalScore = exerciseTaken.getTotalScore();
				LocalDateTime createdDate = exerciseTaken.getCreatedDate();
				ExerciseTakenResponseDTO exerciseTakenResponseDTO = new ExerciseTakenResponseDTO(id, totalScore,
						createdDate);
				exerciseTakenResponseDTOList.add(exerciseTakenResponseDTO);
			}
		}
		return exerciseTakenResponseDTOList;
	}

	@Override
	public String doExercise(ExerciseTakenRequestDTO exerciseTakenRequestDTO) {
		long accountId = exerciseTakenRequestDTO.getAccountId();
		long exerciseId = exerciseTakenRequestDTO.getExerciseId();

		try {
			Account account = iAccountRepository.findByIdAndStatus(accountId, ACTIVE_STATUS);
			if (account == null) {
				throw new ResourceNotFoundException();
			}
			Exercise exercise = iExerciseRepository.findByIdAndIsDisableFalse(exerciseId);
			if (exercise == null) {
				throw new ResourceNotFoundException();
			}

			ExerciseTaken exerciseTaken = modelMapper.map(exerciseTakenRequestDTO, ExerciseTaken.class);
			iExerciseTakenRepository.save(exerciseTaken);
		} catch (Exception e) {
			logger.error("DO EXERICSE: id = " + exerciseId + " by accountId =  " + accountId + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "DO FAIL!";
		}

		return "DO SUCCESS!";
	}

	@Override
	public int countExerciseNotDone(long accountId, long exerciseId) {
		int countNotDone = 0;
		try {
			List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository.findByExerciseIdAndAccountId(exerciseId,
					accountId);
			if (exerciseTakenList.isEmpty()) {
				countNotDone = 1;
			}
		} catch (Exception e) {
			throw e;
		}

		return countNotDone;
	}

}
