package yb.weatherforcast.domain.model

enum class BusinessDataState {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR,
    UNKNOWN_STATE,
}