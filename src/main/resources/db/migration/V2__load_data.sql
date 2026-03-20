COPY exercise(muscle_group, muscle_detail, exercise_name, description)
    FROM '${csv_path}/exercises.csv'
    DELIMITER ','
    CSV HEADER;

COPY food(barcode, name, brands, kcal, proteins, fats, carbs)
    FROM '${csv_path}/food.csv'
    DELIMITER ','
    CSV HEADER;