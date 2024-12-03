# Załaduj potrzebne pakiety
library(ggplot2)
library(dplyr)

# Funkcja do wczytania i przygotowania danych z pliku
load_and_prepare_data <- function(file_path, source_name) {
  data <- read.csv(file_path, sep = "|", header = FALSE, stringsAsFactors = FALSE)
  colnames(data) <- c("Operation", "ExecutionTime")
  data$ExecutionTime <- as.numeric(data$ExecutionTime)
  data <- data %>% filter(!is.na(ExecutionTime)) # Usuń wiersze z NA
  data$ExecutionTime <- data$ExecutionTime / 1e6 # Konwersja na milisekundy
  data$Source <- source_name
  return(data)
}

# Lista plików Oracle i Cassandra
oracle_files <- c("oracle_10.csv", "oracle_100.csv", "oracle_1_000.csv", "oracle_10_000.csv", "oracle_100_000.csv", "oracle_1_000_000.csv")
cassandra_files <- c("cassandra_10.csv", "cassandra_100.csv", "cassandra_1_000.csv", "cassandra_10_000.csv", "cassandra_100_000.csv", "cassandra_1_000_000.csv")

# Definicja kolorów
colors <- c("Oracle" = "skyblue", "Cassandra" = "orange")

# Iteracja przez pliki i tworzenie histogramów
for (i in seq_along(oracle_files)) {
  # Wczytaj dane z Oracle i Cassandra
  oracle_data <- load_and_prepare_data(oracle_files[i], "Oracle")
  cassandra_data <- load_and_prepare_data(cassandra_files[i], "Cassandra")

  # Połącz dane
  combined_data <- bind_rows(oracle_data, cassandra_data)

  # Tworzenie histogramu
  plot <- ggplot(combined_data, aes(x = ExecutionTime, fill = Source)) +
    geom_histogram(binwidth = 0.1, position = "dodge", alpha = 0.7, color = "black") +
    scale_fill_manual(values = colors) +  # Ustawianie kolorów dla Oracle i Cassandra
    labs(
      title = paste("Rozkład czasów wykonania operacji (Histogram) -", oracle_files[i]),
      x = "Czas wykonania (ms)",
      y = "Liczba operacji",
      fill = "Baza danych"
    ) +
    theme_dark() +  # Ciemny motyw
    theme(axis.text.x = element_text(angle = 45, hjust = 1))  # Obrócenie etykiet na osi X

  # Zapisz wykres jako PNG
  output_file <- paste0("comparison_", gsub(".csv", "", oracle_files[i]), "_histogram_dark.png")
  ggsave(output_file, plot = plot, width = 10, height = 6)
}
