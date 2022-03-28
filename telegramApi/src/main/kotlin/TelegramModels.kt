package com.io.telegram

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

sealed class TelegramModel
sealed class InputMedia : TelegramModel()
sealed class InputMessageContent : TelegramModel()
sealed class InlineQueryResult : TelegramModel()
sealed class PassportElementError : TelegramModel()

@Serializable
sealed class ReplyKeyboard: TelegramModel()

@Serializable
data class Update(
    val update_id: Int,
    val message: Message? = null,
    val edited_message: Message? = null,
    val channel_post: Message? = null,
    val edited_channel_post: Message? = null,
    val inline_query: InlineQuery? = null,
    val chosen_inline_result: ChosenInlineResult? = null,
    val callback_query: CallbackQuery? = null,
    val shipping_query: ShippingQuery? = null,
    val pre_checkout_query: PreCheckoutQuery? = null,
    val poll: Poll? = null
) : TelegramModel() {

    fun hasMessage() = message != null

    fun hasCallbackQuery() = callback_query != null
}

@Serializable
data class WebhookInfo(
    val url: String,
    val has_custom_certificate: Boolean,
    val pending_update_count: Int,
    val last_error_date: Int? = null,
    val last_error_message: String? = null,
    val max_connections: Int? = null,
    val allowed_updates: List<String>? = null
) : TelegramModel()

@Serializable
data class User(
    val id: String,
    val is_bot: Boolean = false,
    val first_name: String,
    val last_name: String? = null,
    val username: String? = null,
    val language_code: String? = null
) : TelegramModel()

@Serializable
data class Chat(
    val id: String,
    val type: String,
    val title: String? = null,
    val username: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val all_members_are_administrators: Boolean? = null,
    val photo: ChatPhoto? = null,
    val description: String? = null,
    val invite_link: String? = null,
    val pinned_message: Message? = null,
    val sticker_set_name: String? = null,
    val can_set_sticker_set: Boolean? = null
) : TelegramModel()

@Serializable
data class Message(
    val message_id: Int,
    val from: User? = null,
    val date: Long,
    val chat: Chat,
    val forward_from: User? = null,
    val forward_from_chat: Chat? = null,
    val forward_from_message_id: Int? = null,
    val forward_signature: String? = null,
    val forward_sender_name: String? = null,
    val forward_date: Int? = null,
    val reply_to_message: Message? = null,
    val edit_date: Int? = null,
    val media_group_id: String? = null,
    val author_signature: String? = null,
    val text: String? = null,
    val entities: List<MessageEntity>? = null,
    val caption_entities: List<MessageEntity>? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    val animation: Animation? = null,
    val game: Game? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val video: Video? = null,
    val voice: Voice? = null,
    val video_note: VideoNote? = null,
    val caption: String? = null,
    val contact: Contact? = null,
    val location: Location? = null,
    val venue: Venue? = null,
    val poll: Poll? = null,
    val new_chat_members: List<User>? = null,
    val left_chat_member: User? = null,
    val new_chat_title: String? = null,
    val new_chat_photo: List<PhotoSize>? = null,
    val delete_chat_photo: Boolean? = null,
    val group_chat_created: Boolean? = null,
    val supergroup_chat_created: Boolean? = null,
    val channel_chat_created: Boolean? = null,
    val migrate_to_chat_id: Int? = null,
    val migrate_from_chat_id: Int? = null,
    val pinned_message: Message? = null,
    val invoice: Invoice? = null,
    val successful_payment: SuccessfulPayment? = null,
    val connected_website: String? = null,
    val passport_data: PassportData? = null,
    val reply_markup: InlineKeyboardMarkup? = null
) : TelegramModel() {

    fun hasText() = !text.isNullOrBlank()
}

@Serializable
data class MessageEntity(
    val type: String,
    val offset: Int,
    val length: Int,
    val url: String? = null,
    val user: User? = null
) : TelegramModel()

@Serializable
data class PhotoSize(
    val file_id: String,
    val width: Int,
    val height: Int,
    val file_size: Int? = null
) : TelegramModel()

@Serializable
data class Audio(
    val file_id: String,
    val duration: Int,
    val performer: String? = null,
    val title: String? = null,
    val mime_type: String? = null,
    val file_size: Int? = null,
    val thumb: PhotoSize? = null
) : TelegramModel()

@Serializable
data class Document(
    val file_id: String,
    val thumb: PhotoSize? = null,
    val file_name: String? = null,
    val mime_type: String? = null,
    val file_size: Int? = null
) : TelegramModel()

@Serializable
data class Video(
    val file_id: String,
    val width: Int,
    val height: Int,
    val duration: Int,
    val thumb: PhotoSize? = null,
    val mime_type: String? = null,
    val file_size: Int? = null
) : TelegramModel()

@Serializable
data class Animation(
    val file_id: String,
    val width: Int,
    val height: Int,
    val duration: Int,
    val thumb: PhotoSize? = null,
    val file_name: String? = null,
    val mime_type: String? = null,
    val file_size: Int? = null
) : TelegramModel()

@Serializable
data class Voice(
    val file_id: String,
    val duration: Int,
    val mime_type: String? = null,
    val file_size: Int? = null
) : TelegramModel()

@Serializable
data class VideoNote(
    val file_id: String,
    val length: Int,
    val duration: Int,
    val thumb: PhotoSize? = null,
    val file_size: Int? = null
) : TelegramModel()

@Serializable
data class Contact(
    val phone_number: String,
    val first_name: String,
    val last_name: String? = null,
    val user_id: Int? = null,
    val vcard: String? = null
) : TelegramModel()

@Serializable
data class Location(
    val longitude: Float,
    val latitude: Float
) : TelegramModel()

@Serializable
data class Venue(
    val location: Location,
    val title: String,
    val address: String,
    val foursquare_id: String? = null,
    val foursquare_type: String? = null
) : TelegramModel()

@Serializable
data class PollOption(
    val text: String,
    val voter_count: Int
) : TelegramModel()

@Serializable
data class Poll(
    val id: String,
    val question: String,
    val options: List<PollOption>,
    val is_closed: Boolean
) : TelegramModel()


@Serializable
data class UserProfilePhotos(
    val total_count: Int,
    val photos: List<List<PhotoSize>>
) : TelegramModel()


@Serializable
data class File(
    val file_id: String,
    val file_size: Int? = null,
    val file_path: String? = null
) : TelegramModel()

@Serializable
data class ReplyKeyboardMarkup(
    val keyboard: List<List<KeyboardButton>>,
    val resize_keyboard: Boolean? = null,
    val one_time_keyboard: Boolean? = null,
    val selective: Boolean? = null
) : ReplyKeyboard()


@Serializable
data class KeyboardButton(
    val text: String,
    val request_contact: Boolean? = null,
    val request_location: Boolean? = null
) : TelegramModel()


@Serializable
data class ReplyKeyboardRemove(
    val remove_keyboard: Boolean,
    val selective: Boolean? = null
) : ReplyKeyboard()


@Serializable
data class InlineKeyboardMarkup(
    val inline_keyboard: List<List<InlineKeyboardButton>>
) : ReplyKeyboard()


@Serializable
data class InlineKeyboardButton(
    val text: String,
    val url: String? = null,
    val login_url: LoginUrl? = null,
    val callback_data: String? = null,
    val switch_inline_query: String? = null,
    val switch_inline_query_current_chat: String? = null,
    val pay: Boolean? = null
) : TelegramModel()


@Serializable
data class LoginUrl(
    val url: String,
    val forward_text: String? = null,
    val bot_username: String? = null,
    val request_write_access: Boolean? = null
) : TelegramModel()

@Serializable
data class CallbackQuery(
    val id: String,
    val from: User,
    val message: Message? = null,
    val inline_message_id: String? = null,
    val chat_instance: String,
    val data: String? = null,
    val game_short_name: String? = null
) : TelegramModel()


@Serializable
data class ForceReply(
    val force_reply: Boolean,
    val selective: Boolean? = null
) : TelegramModel()

@Serializable
data class ChatPhoto(
    val small_file_id: String,
    val big_file_id: String
) : TelegramModel()

@Serializable
data class ChatMember(
    val user: User,
    val status: String,
    val until_date: Int? = null,
    val can_be_edited: Boolean? = null,
    val can_change_info: Boolean? = null,
    val can_post_messages: Boolean? = null,
    val can_edit_messages: Boolean? = null,
    val can_delete_messages: Boolean? = null,
    val can_invite_users: Boolean? = null,
    val can_restrict_members: Boolean? = null,
    val can_pin_messages: Boolean? = null,
    val can_promote_members: Boolean? = null,
    val is_member: Boolean? = null,
    val can_send_messages: Boolean? = null,
    val can_send_media_messages: Boolean? = null,
    val can_send_other_messages: Boolean? = null,
    val can_add_web_page_previews: Boolean? = null
) : TelegramModel()


@Serializable
data class ResponseParameters(
    val migrate_to_chat_id: Int? = null,
    val retry_after: Int? = null
) : TelegramModel()


@Serializable
data class InputMediaPhoto(
    val type: String,
    val media: String,
    val caption: String? = null,
    val parse_mode: String? = null
) : InputMedia()


@Serializable
data class InputMediaVideo(
    val type: String,
    val media: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val duration: Int? = null,
    val supports_streaming: Boolean? = null
) : InputMedia()

@Serializable
data class InputMediaAnimation(
    val type: String,
    val media: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val duration: Int? = null
) : InputMedia()

@Serializable
data class InputMediaAudio(
    val type: String,
    val media: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val duration: Int? = null,
    val performer: String? = null,
    val title: String? = null
) : InputMedia()

@Serializable
data class InputMediaDocument(
    val type: String,
    val media: String,
    val caption: String? = null,
    val parse_mode: String? = null
) : InputMedia()


// Stickers
@Serializable
data class Sticker(
    val file_id: String,
    val width: Int,
    val height: Int,
    val thumb: PhotoSize? = null,
    val emoji: String? = null,
    val set_name: String? = null,
    val mask_position: MaskPosition? = null,
    val file_size: Int? = null
) : TelegramModel()

@Serializable
data class StickerSet(
    val name: String,
    val title: String,
    val contains_masks: Boolean,
    val stickers: List<Sticker>
) : TelegramModel()

@Serializable
data class MaskPosition(
    val point: String,
    val x_shift: Float,
    val y_shift: Float,
    val scale: Float
) : TelegramModel()

@Serializable
data class InlineQuery(
    val id: String,
    val from: User,
    val location: Location? = null,
    val query: String,
    val offset: String
) : TelegramModel()

@Serializable
data class InlineQueryResultArticle(
    val type: String,
    val id: String,
    val title: String,
    val reply_markup: InlineKeyboardMarkup? = null,
    val url: String? = null,
    val hide_url: Boolean? = null,
    val description: String? = null,
    val thumb_url: String? = null,
    val thumb_width: Int? = null,
    val thumb_height: Int? = null
) : InlineQueryResult()

@Serializable
data class InlineQueryResultPhoto(
    val type: String,
    val id: String,
    val photo_url: String,
    val thumb_url: String,
    val photo_width: Int? = null,
    val photo_height: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult()

@Serializable
data class InlineQueryResultGif(
    val type: String,
    val id: String,
    val gif_url: String,
    val gif_width: Int? = null,
    val gif_height: Int? = null,
    val gif_duration: Int? = null,
    val thumb_url: String,
    val title: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult()

@Serializable
data class InlineQueryResultMpeg4Gif(
    val type: String,
    val id: String,
    val mpeg4_url: String,
    val mpeg4_width: Int? = null,
    val mpeg4_height: Int? = null,
    val mpeg4_duration: Int? = null,
    val thumb_url: String,
    val title: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult()

@Serializable
data class InlineQueryResultVideo(
    val type: String,
    val id: String,
    val video_url: String,
    val mime_type: String,
    val thumb_url: String,
    val title: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val video_width: Int? = null,
    val video_height: Int? = null,
    val video_duration: Int? = null,
    val description: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult()

@Serializable
data class InlineQueryResultAudio(
    val type: String,
    val id: String,
    val audio_url: String,
    val title: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val performer: String? = null,
    val audio_duration: Int? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult()

@Serializable
data class InlineQueryResultVoice(
    val type: String,
    val id: String,
    val voice_url: String,
    val title: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val voice_duration: Int? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult()

@Serializable
data class InlineQueryResultDocument(
    val type: String,
    val id: String,
    val title: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val document_url: String,
    val mime_type: String,
    val description: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val thumb_url: String? = null,
    val thumb_width: Int? = null,
    val thumb_height: Int? = null
) : InlineQueryResult()

@Serializable
data class InlineQueryResultLocation(
    val type: String,
    val id: String,
    val latitude: Float,
    val longitude: Float,
    val title: String,
    val live_period: Int? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val thumb_url: String? = null,
    val thumb_width: Int? = null,
    val thumb_height: Int? = null
) : InlineQueryResult()

@Serializable
data class InlineQueryResultVenue(
    val type: String,
    val id: String,
    val latitude: Float,
    val longitude: Float,
    val title: String,
    val address: String,
    val foursquare_id: String? = null,
    val foursquare_type: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val thumb_url: String? = null,
    val thumb_width: Int? = null,
    val thumb_height: Int? = null
) : InlineQueryResult()

@Serializable
data class InlineQueryResultContact(
    val type: String,
    val id: String,
    val phone_number: String,
    val first_name: String,
    val last_name: String? = null,
    val vcard: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
    val thumb_url: String? = null,
    val thumb_width: Int? = null,
    val thumb_height: Int? = null
) : InlineQueryResult()

@Serializable
data class InlineQueryResultGame(
    val type: String,
    val id: String,
    val game_short_name: String,
    val reply_markup: InlineKeyboardMarkup? = null
) : InlineQueryResult()

@Serializable
data class InlineQueryResultCachedPhoto(
    val type: String,
    val id: String,
    val photo_file_id: String,
    val title: String? = null,
    val description: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult()

@Serializable
data class InlineQueryResultCachedGif(
    val type: String,
    val id: String,
    val gif_file_id: String,
    val title: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult()

@Serializable
data class InlineQueryResultCachedMpeg4Gif(
    val type: String,
    val id: String,
    val mpeg4_file_id: String,
    val title: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult()

@Serializable
data class InlineQueryResultCachedSticker(
    val type: String,
    val id: String,
    val sticker_file_id: String,
    val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult()

@Serializable
data class InlineQueryResultCachedDocument(
    val type: String,
    val id: String,
    val title: String,
    val document_file_id: String,
    val description: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null
) : InlineQueryResult()

@Serializable
data class InlineQueryResultCachedVideo(
    val type: String,
    val id: String,
    val video_file_id: String,
    val title: String,
    val description: String? = null,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult()


@Serializable
data class InlineQueryResultCachedVoice(
    val type: String,
    val id: String,
    val voice_file_id: String,
    val title: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult()

@Serializable
data class InlineQueryResultCachedAudio(
    val type: String,
    val id: String,
    val audio_file_id: String,
    val caption: String? = null,
    val parse_mode: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult()

@Serializable
data class InputTextMessageContent(
    val message_text: String,
    val parse_mode: String? = null,
    val disable_web_page_preview: Boolean? = null
) : TelegramModel()

@Serializable
data class InputLocationMessageContent(
    val latitude: Float,
    val longitude: Float,
    val live_period: Int? = null
) : TelegramModel()


@Serializable
data class InputVenueMessageContent(
    val latitude: Float,
    val longitude: Float,
    val title: String,
    val address: String,
    val foursquare_id: String? = null,
    val foursquare_type: String? = null
) : TelegramModel()


@Serializable
data class InputContactMessageContent(
    val phone_number: String,
    val first_name: String,
    val last_name: String? = null,
    val vcard: String? = null
) : TelegramModel()


@Serializable
data class ChosenInlineResult(
    val result_id: String,
    val from: User,
    val location: Location? = null,
    val inline_message_id: String? = null,
    val query: String
) : TelegramModel()


// Payments


@Serializable
data class LabeledPrice(
    val label: String,
    val amount: Int
) : TelegramModel()

@Serializable
data class Invoice(
    val title: String,
    val description: String,
    val start_parameter: String,
    val currency: String,
    val total_amount: Int
) : TelegramModel()

@Serializable
data class ShippingAddress(
    val country_code: String,
    val state: String,
    val city: String,
    val street_line1: String,
    val street_line2: String,
    val post_code: String
) : TelegramModel()

@Serializable
data class OrderInfo(
    val name: String? = null,
    val phone_number: String? = null,
    val email: String? = null,
    val shipping_address: ShippingAddress? = null
) : TelegramModel()


@Serializable
data class ShippingOption(
    val id: String,
    val title: String,
    val prices: List<LabeledPrice>
) : TelegramModel()

@Serializable
data class SuccessfulPayment(
    val currency: String,
    val total_amount: Int,
    val invoice_payload: String,
    val shipping_option_id: String? = null,
    val order_info: OrderInfo? = null,
    val telegram_payment_charge_id: String,
    val provider_payment_charge_id: String
) : TelegramModel()

@Serializable
data class ShippingQuery(
    val id: String,
    val from: User,
    val invoice_payload: String,
    val shipping_address: ShippingAddress
) : TelegramModel()


@Serializable
data class PreCheckoutQuery(
    val id: String,
    val from: User,
    val currency: String,
    val total_amount: Int,
    val invoice_payload: String,
    val shipping_option_id: String? = null,
    val order_info: OrderInfo? = null
) : TelegramModel()


// Telegram Passport

@Serializable
data class PassportData(
    val data: List<EncryptedPassportElement>,
    val credentials: EncryptedCredentials
) : TelegramModel()

@Serializable
data class PassportFile(
    val file_id: String,
    val file_size: Int,
    val file_date: Int
) : TelegramModel()

@Serializable
data class EncryptedPassportElement(
    val type: String,
    val data: String? = null,
    val phone_number: String? = null,
    val email: String? = null,
    val files: List<PassportFile>? = null,
    val front_side: PassportFile? = null,
    val reverse_side: PassportFile? = null,
    val selfie: PassportFile? = null,
    val translation: List<PassportFile>? = null,
    val hash: String
) : TelegramModel()

@Serializable
data class EncryptedCredentials(
    val data: String,
    val hash: String,
    val secret: String
) : TelegramModel()


@Serializable
data class PassportElementErrorDataField(
    val source: String,
    val type: String,
    val field_name: String,
    val data_hash: String,
    val message: String
) : PassportElementError()


@Serializable
data class PassportElementErrorFrontSide(
    val source: String,
    val type: String,
    val file_hash: String,
    val message: String
) : PassportElementError()


@Serializable
data class PassportElementErrorReverseSide(
    val source: String,
    val type: String,
    val file_hash: String,
    val message: String
) : PassportElementError()


@Serializable
data class PassportElementErrorSelfie(
    val source: String,
    val type: String,
    val file_hash: String,
    val message: String
) : PassportElementError()


@Serializable
data class PassportElementErrorFile(
    val source: String,
    val type: String,
    val file_hash: String,
    val message: String
) : PassportElementError()

@Serializable
data class PassportElementErrorFiles(
    val source: String,
    val type: String,
    val file_hashes: List<String>,
    val message: String
) : PassportElementError()

@Serializable
data class PassportElementErrorTranslationFile(
    val source: String,
    val type: String,
    val file_hash: String,
    val message: String
) : PassportElementError()

@Serializable
data class PassportElementErrorTranslationFiles(
    val source: String,
    val type: String,
    val file_hashes: List<String>,
    val message: String
) : PassportElementError()


@Serializable
data class PassportElementErrorUnspecified(
    val source: String,
    val type: String,
    val element_hash: String,
    val message: String
) : PassportElementError()


// Games

@Serializable
data class Game(
    val title: String,
    val description: String,
    val photo: List<PhotoSize>,
    val text: String? = null,
    val text_entities: List<MessageEntity>? = null,
    val animation: Animation? = null
) : TelegramModel()

@Serializable
data class GameHighScore(
    val position: Int,
    val user: User,
    val score: Int
) : TelegramModel()
