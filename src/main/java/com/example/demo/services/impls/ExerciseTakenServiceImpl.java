package com.example.demo.services.impls;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.ExerciseResponseDTO;
import com.example.demo.dtos.ExerciseTakenRequestDTO;
import com.example.demo.dtos.ExerciseTakenResponseDTO;
import com.example.demo.dtos.LessonScoreViewDTO;
import com.example.demo.dtos.ScoreResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Account;
import com.example.demo.models.Exercise;
import com.example.demo.models.ExerciseTaken;
import com.example.demo.models.Lesson;
import com.example.demo.models.ProgressTest;
import com.example.demo.models.StudentRecord;
import com.example.demo.models.Unit;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.IExerciseTakenRepository;
import com.example.demo.repositories.ILessonRepository;
import com.example.demo.repositories.IProgressTestRepository;
import com.example.demo.repositories.IStudentRecordRepository;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.IExerciseTakenService;

@Service
public class ExerciseTakenServiceImpl implements IExerciseTakenService {
	Logger logger = LoggerFactory.getLogger(ExerciseTakenServiceImpl.class);

	private final String ACTIVE_STATUS = "ACTIVE";
	private final String DELETED_STATUS = "DELETED";

	@Autowired
	private IExerciseTakenRepository iExerciseTakenRepository;

	@Autowired
	private IAccountRepository iAccountRepository;

	@Autowired
	private IExerciseRepository iExerciseRepository;

	@Autowired
	private IUnitRepository iUnitRepository;

	@Autowired
	private ILessonRepository iLessonRepository;

	@Autowired
	private IProgressTestRepository iProgressTestRepository;

	@Autowired
	private IStudentRecordRepository iStudentRecordRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public String findTakenObjectById(long id) {
		String takenObject = null;
		ExerciseTaken exerciseTaken = iExerciseTakenRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
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
				Exercise exercise = iExerciseRepository.findByIdAndStatusNot(exerciseTaken.getExerciseId(),
						DELETED_STATUS);
				exerciseTakenResponseDTO.setExerciseName("Exercise" + exercise.getExerciseName());
				exerciseTakenResponseDTOList.add(exerciseTakenResponseDTO);
			}
		}
		return exerciseTakenResponseDTOList;
	}

	@Override
	@Transactional
	public String doExercise(ExerciseTakenRequestDTO exerciseTakenRequestDTO) {
		long accountId = exerciseTakenRequestDTO.getAccountId();
		long exerciseId = exerciseTakenRequestDTO.getExerciseId();

		try {
			Account account = iAccountRepository.findByIdAndStatus(accountId, ACTIVE_STATUS);
			if (account == null) {
				throw new ResourceNotFoundException();
			}
			Exercise exercise = iExerciseRepository.findByIdAndStatusNot(exerciseId, DELETED_STATUS);
			if (exercise == null) {
				throw new ResourceNotFoundException();
			}

			ExerciseTaken exerciseTaken = modelMapper.map(exerciseTakenRequestDTO, ExerciseTaken.class);

			iExerciseTakenRepository.save(exerciseTaken);

			long lessonId = exercise.getLessonId();
			long unitId = 0;
			if (lessonId != 0) {
				Lesson lesson = iLessonRepository.findByIdAndIsDisableFalse(lessonId);
				if (lesson != null) {
					unitId = lesson.getUnitId();
				}
			}
			long progressTestId = exercise.getProgressTestId();

			StudentRecord studentRecord = iStudentRecordRepository.findByUnitIdAndAccountId(unitId, accountId);
			if (studentRecord == null) {
				studentRecord = new StudentRecord();
				studentRecord.setAccountId(accountId);
				if (exercise.isProgressTest()) {
					studentRecord.setProgressTestId(progressTestId);
					studentRecord.setUnitId(0);
				} else {
					studentRecord.setProgressTestId(0);
					studentRecord.setUnitId(unitId);
				}
				studentRecord.setListExerciseTakenScore(String.valueOf(exerciseTakenRequestDTO.getTotalScore() + " "));
				studentRecord.setAverageScore(exerciseTakenRequestDTO.getTotalScore());
				iStudentRecordRepository.save(studentRecord);
			} else {
				String listExerciseTakenScore = studentRecord.getListExerciseTakenScore();
				listExerciseTakenScore += String.valueOf(" " + exerciseTakenRequestDTO.getTotalScore() + " ");
				String[] scoreList = listExerciseTakenScore.split(" ");

				float totalScore = 0;
				if (scoreList.length > 0) {
					for (String score : scoreList) {
						System.out.println("start debug: " + score);
						
						totalScore += Double.parseDouble(score);
						System.out.println("error");
					}
					studentRecord.setAverageScore(totalScore / scoreList.length);
				}

				studentRecord.setListExerciseTakenScore(listExerciseTakenScore);
				iStudentRecordRepository.save(studentRecord);
			}

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

	// unitName: process
	// subjectId --> all unit + all progressTest
	// unit --> all lesson --> all exercise
	// progressTest --> lesson = 0 --> all exercise

	@Override
	public List<ScoreResponseDTO> findAllExerciseScoreBySubjectId(long subjectId, long accountId) {
		List<ScoreResponseDTO> scoreResponseDTOList = new ArrayList<>();
		// find all unit and progressTest
		try {
			List<Unit> unitList = iUnitRepository.findBySubjectIdAndIsDisableFalseOrderByUnitNameAsc(subjectId);
			List<ProgressTest> progressTestList = iProgressTestRepository.findBySubjectIdAndIsDisableFalse(subjectId);

			// order unit and progressTest
			Map<String, Long> orderMap = new LinkedHashMap<>();
			if (!unitList.isEmpty()) {
				for (Unit unit : unitList) {
					orderMap.put(String.valueOf(unit.getUnitName()), unit.getId());
					if (!progressTestList.isEmpty()) {
						for (ProgressTest progressTest : progressTestList) {
							if (unit.getId() == progressTest.getUnitAfterId()) {
								orderMap.put(progressTest.getProgressTestName(), progressTest.getId());
							}
						}
					}
				}
			}

			for (Entry<String, Long> entry : orderMap.entrySet()) {
				List<LessonScoreViewDTO> lessonScoreViewDTOList = new ArrayList<>();

				String unitName = entry.getKey();
				String process = "N/A";
				int totalExericse = 0;
				int countNotDone = 0;

				if (unitName.contains("Review") || unitName.contains("Semester")) {
					List<Exercise> exerciseList = new ArrayList<>();
					long progressTestId = entry.getValue();
					// get all exercise by progress id
					exerciseList.addAll(iExerciseRepository
							.findByProgressTestIdAndStatusNotOrderByExerciseNameAsc(progressTestId, DELETED_STATUS));
					totalExericse = 0 + exerciseList.size();
					List<ExerciseResponseDTO> exerciseResponseDTOList = new ArrayList<>();
					for (Exercise exercise : exerciseList) {
						ExerciseResponseDTO exerciseResponseDTO = modelMapper.map(exercise, ExerciseResponseDTO.class);
						List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository
								.findByExerciseIdAndAccountId(exercise.getId(), accountId);
						if (!exerciseTakenList.isEmpty()) {
							exerciseResponseDTO.setDone(true);
						} else {
							countNotDone++;
						}
						exerciseResponseDTOList.add(exerciseResponseDTO);
					}
					LessonScoreViewDTO lessonScoreViewDTO = new LessonScoreViewDTO(0, exerciseResponseDTOList);
					lessonScoreViewDTOList.add(lessonScoreViewDTO);

				} else {
					List<Lesson> lessonList = new ArrayList<>();
					long unitId = entry.getValue();
					unitName = "Unit " + entry.getKey();
					lessonList.addAll(iLessonRepository.findByUnitIdAndIsDisableFalse(unitId));
					if (!lessonList.isEmpty()) {
						for (Lesson lesson : lessonList) {
							int lessonName = lesson.getLessonName();
							// find all exercise of lesson
							List<ExerciseResponseDTO> exerciseResponseDTOList = new ArrayList<>();
							List<Exercise> exerciseList = new ArrayList<>();
							long lessonId = lesson.getId();
							exerciseList
									.addAll(iExerciseRepository.findByLessonIdAndStatusNotOrderByExerciseNameAsc(lessonId, DELETED_STATUS));
							totalExericse += exerciseList.size();

							if (!exerciseList.isEmpty()) {
								for (Exercise exercise : exerciseList) {
									ExerciseResponseDTO exerciseResponseDTO = modelMapper.map(exercise,
											ExerciseResponseDTO.class);
									// check exercise isTaken
									List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository
											.findByExerciseIdAndAccountId(exercise.getId(), accountId);
									if (!exerciseTakenList.isEmpty()) {
										exerciseResponseDTO.setDone(true);
									} else {
										countNotDone++;
									}
									exerciseResponseDTOList.add(exerciseResponseDTO);
								}
							}

							LessonScoreViewDTO lessonScoreViewDTO = new LessonScoreViewDTO(lessonName,
									exerciseResponseDTOList);
							lessonScoreViewDTOList.add(lessonScoreViewDTO);
						}
					}
				}
				if (totalExericse > 0) {
					double percentDone = (totalExericse - countNotDone) * 100 / totalExericse;
					process = (int) Math.round(percentDone) + "%";
				}

				ScoreResponseDTO scoreResponseDTO = new ScoreResponseDTO();
				scoreResponseDTO.setUnitName(unitName);
				scoreResponseDTO.setProcess(process);
				scoreResponseDTO.setLessonScoreViewDTOList(lessonScoreViewDTOList);
				scoreResponseDTOList.add(scoreResponseDTO);
			}
		} catch (Exception e) {
			logger.error("FIND: all score of all exercise by subjectId = " + subjectId + "! " + e.getMessage());

			return null;
		}

		return scoreResponseDTOList;
	}

}
