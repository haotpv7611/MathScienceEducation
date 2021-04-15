package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ExerciseResponseDTO;
import com.example.demo.dtos.LessonScoreViewDTO;
import com.example.demo.dtos.ScoreResponseDTO;
import com.example.demo.models.Exercise;
import com.example.demo.models.ExerciseTaken;
import com.example.demo.models.Lesson;
import com.example.demo.models.ProgressTest;
import com.example.demo.models.Unit;
import com.example.demo.repositories.IExerciseRepository;
import com.example.demo.repositories.IExerciseTakenRepository;
import com.example.demo.repositories.ILessonRepository;
import com.example.demo.repositories.IProgressTestRepository;
import com.example.demo.repositories.IUnitRepository;
import com.example.demo.services.IScoreService;

@Service
public class ScoreServiceImpl implements IScoreService {
	Logger logger = LoggerFactory.getLogger(ScoreServiceImpl.class);

	private final String DELETED_STATUS = "DELETED";
	@Autowired
	private IUnitRepository iUnitRepository;

	@Autowired
	private ILessonRepository iLessonRepository;

	@Autowired
	private IExerciseRepository iExerciseRepository;

	@Autowired
	private IExerciseTakenRepository iExerciseTakenRepository;

	@Autowired
	private IProgressTestRepository iProgressTestRepository;

	@Autowired
	private ModelMapper modelMapper;

	// unitName: process

	@Override
	public List<ScoreResponseDTO> findAllExerciseScoreBySubjectId(long subjectId, long accountId) {
		List<ScoreResponseDTO> scoreResponseDTOList = new ArrayList<>();
		List<Unit> unitList = iUnitRepository.findBySubjectIdAndIsDisableFalseOrderByUnitNameAsc(subjectId);
		List<ProgressTest> progressTestList = iProgressTestRepository.findBySubjectIdAndIsDisableFalse(subjectId);
		if (!unitList.isEmpty()) {
			for (Unit unit : unitList) {

				int unitName = unit.getUnitName();
				// no exercise exited
				String process = "N/A";
				int totalExericse = 0;
				int countNotDone = 0;
				List<LessonScoreViewDTO> lessonScoreViewDTOList = new ArrayList<>();

				List<Lesson> lessonList = new ArrayList<>();
				long unitId = unit.getId();
				lessonList.addAll(iLessonRepository.findByUnitIdAndIsDisableFalse(unitId));
				if (!lessonList.isEmpty()) {
					for (Lesson lesson : lessonList) {
						int lessonName = lesson.getLessonName();
						List<ExerciseResponseDTO> exerciseResponseDTOList = new ArrayList<>();

						List<Exercise> exerciseList = new ArrayList<>();
						long lessonId = lesson.getId();
						exerciseList.addAll(iExerciseRepository.findByLessonIdAndStatusNot(lessonId, DELETED_STATUS));
						totalExericse += exerciseList.size();

						for (Exercise exercise : exerciseList) {
							ExerciseResponseDTO exerciseResponseDTO = modelMapper.map(exercise,
									ExerciseResponseDTO.class);
							List<ExerciseTaken> exerciseTakenList = iExerciseTakenRepository
									.findByExerciseIdAndAccountId(exercise.getId(), accountId);
							if (!exerciseTakenList.isEmpty()) {
								exerciseResponseDTO.setDone(true);
							} else {
								countNotDone++;
							}
							exerciseResponseDTOList.add(exerciseResponseDTO);
						}

						LessonScoreViewDTO lessonScoreViewDTO = new LessonScoreViewDTO(lessonName,
								exerciseResponseDTOList);
						lessonScoreViewDTOList.add(lessonScoreViewDTO);
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

				if (!progressTestList.isEmpty()) {
					List<ExerciseResponseDTO> exerciseResponseDTOList = new ArrayList<>();
					for (int i = 0; i < progressTestList.size(); i++) {
						if (progressTestList.get(i).getUnitAfterId() == unit.getId()) {
							List<Exercise> exerciseList = new ArrayList<>();
							long progressTestId = progressTestList.get(i).getId();

							// get all exercise by progress id
							exerciseList.addAll(iExerciseRepository.findByProgressTestIdAndStatusNot(progressTestId,
									DELETED_STATUS));
							totalExericse = 0 + exerciseList.size();

							for (Exercise exercise : exerciseList) {
								ExerciseResponseDTO exerciseResponseDTO = modelMapper.map(exercise,
										ExerciseResponseDTO.class);
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

							if (totalExericse > 0) {
								double percentDone = (totalExericse - countNotDone) * 100 / totalExericse;
								process = (int) Math.round(percentDone) + "%";
							}

							scoreResponseDTO = new ScoreResponseDTO();
							scoreResponseDTO.setUnitName(progressTestList.get(i).getProgressTestName());
							scoreResponseDTO.setProcess(process);
							scoreResponseDTO.setLessonScoreViewDTOList(lessonScoreViewDTOList);
							scoreResponseDTOList.add(scoreResponseDTO);
							break;
						}
					}
				}
			}
		}

		return scoreResponseDTOList;
	}

}
