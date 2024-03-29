import {createStore} from 'vuex'

export const store = createStore({
  state: {
    // The time-recording application itself
    timeRecorderDto: '',
    // the current business-day
    businessDay: {
      businessDayIncrementDtos: '',
    },
    // The overview of all booked business-days
    businessDayHistoryOverviewDto: {
      businessDayHistoryDtos: [],
      ticketDistributionDto: {
        ticketDistributionDtoEntries: []
      }
    }
  },
  getters: {
    ticketDistributionChartData(state, getters) {
      let ticketDistributionsEntries = getters.ticketDistributionDtoEntries;
      return Array.from(ticketDistributionsEntries, ticketDistributionsEntry => [ticketDistributionsEntry.ticketNr, ticketDistributionsEntry.percentage]);
    },
    businessDayHistoryChartData(state, getters) {
      let businessDayHistoryDtos = getters.businessDayHistoryDtos;
      return Array.from(businessDayHistoryDtos, businessDayHistoryDto => [businessDayHistoryDto.dateRepresentation, businessDayHistoryDto.bookedHours]);
    },
    ticketDistributionDtoEntries: state => {
      return state.businessDayHistoryOverviewDto.ticketDistributionDto.ticketDistributionDtoEntries;
    },
    businessDayHistoryDtos: state => {
      return state.businessDayHistoryOverviewDto.businessDayHistoryDtos;
    },
    businessDay: state => {
      return state.businessDay;
    },
    timeRecorderDto: state => {
      return state.timeRecorderDto;
    }
  },
  mutations: {
    setBusinessDayHistoryOverviewDto(state, businessDayHistoryOverviewDto) {
      state.businessDayHistoryOverviewDto = businessDayHistoryOverviewDto
    },
    setBusinessDay(state, businessDay) {
      state.businessDay = businessDay
    },
    setTimeRecorderDto(state, timeRecorderDto) {
      state.timeRecorderDto = timeRecorderDto
    },
  },
  actions: {
    setBusinessDayHistoryOverviewDto(context, businessDayHistoryOverviewDto) {
      context.commit("setBusinessDayHistoryOverviewDto", businessDayHistoryOverviewDto);
    },
    setBusinessDay(context, businessDay) {
      context.commit("setBusinessDay", businessDay);
    },
    setTimeRecorderDto(context, timeRecorderDto) {
      context.commit("setTimeRecorderDto", timeRecorderDto);
    },
  },
});
