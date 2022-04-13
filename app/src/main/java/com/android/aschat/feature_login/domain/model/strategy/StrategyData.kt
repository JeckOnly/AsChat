package com.android.aschat.feature_login.domain.model.strategy

data class StrategyData(
    val broadcasterFollowOfficialUserIds: List<String>,
    val broadcasterWallRegions: List<Any>,
    val broadcasterWallTagList: List<BroadcasterWallTag>,
    val broadcasterWallTags: List<String>,
    val flashChatConfig: FlashChatConfig,
    val freeUserCallStaySecond: String,
    val freeUserImStaySecond: String,
    val genderMatchCoin: GenderMatchCoin,
    val imIncentiveBlacklistUserIds: List<String>,
    val imSessionBalance: Int,
    val imSessionBroadcasterIds: List<Any>,
    val initTab: Int,
    val isAutoAccept: Boolean,
    val isCallRearCamera: Boolean,
    val isDisplayNotDisturbCall: Boolean,
    val isDisplayNotDisturbIm: Boolean,
    val isForceEvaluationInstruct: Boolean,
    val isMaskOpen: Boolean,
    val isMatchCallFree: Boolean,
    val isNewTppUsable: Boolean,
    val isOpenFlashChat: Boolean,
    val isRandomUploadPaidEvents: Boolean,
    val isRearCamera: Boolean,
    val isReviewPkg: Boolean,
    val isShowAutoTranslate: Boolean,
    val isShowBroadcasterRank: Boolean,
    val isShowDeletedButton: Boolean,
    val isShowFlowInfo: Boolean,
    val isShowLP: Boolean,
    val isShowMatch: Boolean,
    val isShowMatchGender: Boolean,
    val isShowRookieGuide: Boolean,
    val isSilence: Boolean,
    val isSwitchClub: Boolean,
    val isSwitchExtraCategory: Boolean,
    val isSwitchIMIncentive: Boolean,
    val isSwitchIMLimit: Boolean,
    val isSwitchInstruct: Boolean,
    val isSwitchMultipleCall: Boolean,
    val isSwitchOneKeyFollow: Boolean,
    val isSwitchStrongGuide: Boolean,
    val lpDiscount: Int,
    val lpPromotionDiscount: Int,
    val officialBlacklistUserIds: List<String>,
    val payChannels: List<String>,
    val rechargeUserCallStaySecond: String,
    val rechargeUserImStaySecond: String,
    val reviewOfficialBlacklistUserIds: List<String>,
    val tabType: Int,
    val timestamp: String,
    val topOfficialUserIds: List<String>,
    val userInvitation: UserInvitation,
    val userServiceAccountId: String,
    val videoStreamCategory: List<String>
)