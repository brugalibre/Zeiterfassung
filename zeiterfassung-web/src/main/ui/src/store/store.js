import {createStore} from 'vuex'

export const store = createStore({
  state: {
    businessDayHistoryOverviewDto: {
      businessDayHistoryDtos: [
        {dateRepresentation: String},
        {date: Date},
        {bookedHours: String}
      ],
      ticketDistributionDto: {
        ticketDistributionDtoEntries: [
          {ticketNr: String},
          {percentage: Number}
        ]
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
    }
  },
  mutations: {
    setBusinessDayHistoryOverviewDto(state, businessDayHistoryOverviewDto) {
      state.businessDayHistoryOverviewDto = businessDayHistoryOverviewDto
    },
  },
  actions: {
    setBusinessDayHistoryOverviewDto(context, businessDayHistoryOverviewDto) {
      context.commit("setBusinessDayHistoryOverviewDto", businessDayHistoryOverviewDto);
    },
  },
});
