# Załaduj potrzebne pakiety
library(ggplot2)
library(dplyr)
library(tidyr)

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
oracle_files <- c("oracle_10.csv", "oracle_100.csv", "oracle_1_000.csv", "oracle_10_000.csv", "oracle_100_000.csv")
cassandra_files <- c("cassandra_10.csv", "cassandra_100.csv", "cassandra_1_000.csv", "cassandra_10_000.csv", "cassandra_100_000.csv")

# Iteracja przez pliki i tworzenie wykresów
for (i in seq_along(oracle_files)) {
  # Wczytaj dane z Oracle i Cassandra
  oracle_data <- load_and_prepare_data(oracle_files[i], "Oracle")
  cassandra_data <- load_and_prepare_data(cassandra_files[i], "Cassandra")

  # Połącz dane
  combined_data <- bind_rows(oracle_data, cassandra_data)

  # Oblicz średnią wartość dla każdej operacji i bazy danych
  avg_data <- combined_data %>%
    group_by(Operation, Source) %>%
    summarise(AverageTime = mean(ExecutionTime), .groups = "drop")

  # Przekształć dane, aby mieć średnie wartości dla Oracle i Cassandra w jednej tabeli
  avg_data_wide <- avg_data %>%
    spread(key = Source, value = AverageTime) %>%
    mutate(
      PercentDifference = (Oracle - Cassandra) / Oracle * 100,  # Oblicz procentową różnicę
      FasterBase = ifelse(Cassandra < Oracle, "Cassandra", "Oracle")  # Która baza jest szybsza
    )

  # Tworzenie wykresu porównawczego dla Oracle i Cassandra w orientacji poziomej (słupki obok siebie)
  plot <- ggplot(avg_data_wide, aes(x = Operation)) +
    geom_bar(aes(y = Oracle, fill = "Oracle"), stat = "identity", position = "dodge", width = 0.4) +  # Słupki Oracle
    geom_bar(aes(y = Cassandra, fill = "Cassandra"), stat = "identity", position = "dodge", width = 0.4) +  # Słupki Cassandra
    scale_fill_manual(values = c("Oracle" = "skyblue", "Cassandra" = "orange")) +
    labs(
      title = paste("Średni czas wykonania Oracle vs Cassandra -", oracle_files[i]),
      x = "Operacja",
      y = "Średni czas wykonania (ms)",
      fill = "Baza danych"
    ) +
    theme_dark() +
    scale_x_discrete(expand = expansion(mult = c(0, 0.1))) +  # Dodaj trochę miejsca w prawym marginesie
    theme(axis.text.y = element_text(angle = 0, hjust = 1))  # Etykiety osi Y

  # Dodanie etykiet z procentową różnicą przy szybszym słupku
  plot <- plot +
    geom_text(data = avg_data_wide, aes(
      x = Operation,
      y = ifelse(FasterBase == "Cassandra", Cassandra, Oracle),  # Pozycja etykiety przy szybszym słupku
      label = paste0(round(abs(PercentDifference), 1), "% szybsza"),  # Etykieta z różnicą procentową
      color = "white",
      hjust = -0.1,  # Wysunięcie etykiety na prawo od słupka
      vjust = 0.5
    ))

  # Zapisz wykres do pliku PNG
  output_file <- paste0("comparison_", gsub(".csv", "", oracle_files[i]), "_avg_speed_comparison_with_percent_horizontal_dodge.png")
  ggsave(output_file, plot, width = 10, height = 6)
}
