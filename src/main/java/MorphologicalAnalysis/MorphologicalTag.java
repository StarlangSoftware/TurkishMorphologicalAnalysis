package MorphologicalAnalysis;

public enum MorphologicalTag {
    /**
     * Noun : Alengir
     */
    NOUN,
    /**
     * Adverb : Alelacele
     */
    ADVERB,
    /**
     * Adjective : Alengirli
     */
    ADJECTIVE,
    /**
     * Verb : Alıkoy
     */
    VERB,
    /**
     * 1st person singular agreement : Ben gelirim
     */
    A1SG,
    /**
     * 2nd person singular agreement : Sen gelirsin
     */
    A2SG,
    /**
     * 3rd person singular agreement : O gelir
     */
    A3SG,
    /**
     * 1st person plural agreement : Biz geliriz
     */
    A1PL,
    /**
     * 2nd person plural agreement : Siz gelirsiniz
     */
    A2PL,
    /**
     * 3rd person plural agreement : Onlar gelirler
     */
    A3PL,
    /**
     * 1st person singular possessive : Benim
     */
    P1SG,
    /**
     * 2nd person singular possessive :Senin
     */
    P2SG,
    /**
     * 3rd person singular possessive : Onun
     */
    P3SG,
    /**
     * 1st person plural possessive :  Bizim
     */
    P1PL,
    /**
     * 2nd person plural possessive : Sizin
     */
    P2PL,
    /**
     * 3rd person plural possessive : Onların
     */
    P3PL,
    /**
     * Proper noun : John
     */
    PROPERNOUN,
    /**
     * None possessive : Ev
     */
    PNON,
    /**
     * Nominative Case : Kedi uyuyor.
     */
    NOMINATIVE,
    /**
     * With : Kalemle
     */
    WITH,
    /**
     * Without : Dikişsiz
     */
    WITHOUT,
    /**
     * Accusatıve : Beni
     */
    ACCUSATIVE,
    /**
     * Dative case : Bana
     */
    DATIVE,
    /**
     * Genitive : Benim
     */
    GENITIVE,
    /**
     * Ablative : Okuldan
     */
    ABLATIVE,
    /**
     * Perosnal pronoun : O
     */
    PERSONALPRONOUN,
    /**
     * Zero Derivation : Kırmızıydı
     */
    ZERO,
    /**
     * Ability, possibility : Olabilir
     */
    ABLE,
    /**
     * Negative : Yapama
     */
    NEGATIVE,
    /**
     * Past tense : Gitti
     */
    PASTTENSE,
    /**
     * Conjunction or disjunction : Ama, ise
     */
    CONJUNCTION,
    /**
     * Determiner : Birtakım
     */
    DETERMINER,
    /**
     * Duplication : Çıtır çıtır
     */
    DUPLICATION,
    /**
     * Interjection : Agucuk
     */
    INTERJECTION,
    /**
     * Number : bir
     */
    NUMBER,
    /**
     * Post posıtıon : Atfen
     */
    POSTPOSITION,
    /**
     * Punctuation : +
     */
    PUNCTUATION,
    /**
     * Question : Mı
     */
    QUESTION,
    /**
     * Agent : Toplayıcı
     */
    AGENT,
    /**
     * By doing so : Zıplayarak
     */
    BYDOINGSO,
    /**
     * Cardinal : yüz, bin
     */
    CARDINAL,
    /**
     * Causative Form : Pişirmek
     */
    CAUSATIVE,
    /**
     * Demonstrative pronoun : Bu, şu
     */
    DEMONSTRATIVEPRONOUN,
    /**
     * Distributive : altışar
     */
    DISTRIBUTIVE,
    /**
     * Fit for : Ahmetlik
     */
    FITFOR,
    /**
     * Future participle : Gülecek
     */
    FUTUREPARTICIPLE,
    /**
     * Infinitive : Biri
     */
    INFINITIVE,
    /**
     * Ness : Ağırbaşlılık
     */
    NESS,
    /**
     * Ordinal Number : Altıncı
     */
    ORDINAL,
    /**
     * Passive : Açıldı
     */
    PASSIVE,
    /**
     * Past participle : Kırılmış
     */
    PASTPARTICIPLE,
    /**
     * Present partıcıple : Sarılan
     */
    PRESENTPARTICIPLE,
    /**
     * Question pronoun : Kim
     */
    QUESTIONPRONOUN,
    /**
     * Quantitative pronoun : Each
     */
    QUANTITATIVEPRONOUN,
    /**
     * Range : 1 - 3
     */
    RANGE,
    /**
     * Ratio : 1/2
     */
    RATIO,
    /**
     * Real : 1.0
     */
    REAL,
    /**
     * Reciprocal verb : Görüşmek
     */
    RECIPROCAL,
    /**
     * Reflexive : Kendi
     */
    REFLEXIVE,
    /**
     * Reflexive pronoun : Kendim
     */
    REFLEXIVEPRONOUN,
    /**
     * Time : 14:28
     */
    TIME,
    /**
     * When : Okuyunca
     */
    WHEN,
    /**
     * While : Gelirken
     */
    WHILE,
    /**
     * Without having done so : Çaktırmadan
     */
    WITHOUTHAVINGDONESO,
    /**
     * PC ablative : Başka
     */
    PCABLATIVE,
    /***
     * PC accusative : Takiben
     */
    PCACCUSATIVE,
    /**
     * PC dative : İlişkin
     */
    PCDATIVE,
    /**
     * PC genitive : Yanısıra
     */
    PCGENITIVE,
    /**
     * PC instrumental : Birlikte
     */
    PCINSTRUMENTAL,
    /**
     * PC nominative
     */
    PCNOMINATIVE,
    /**
     * Acquire : Kazanılan
     */
    ACQUIRE,
    /**
     * Act of : Aldatmaca
     */
    ACTOF,
    /**
     * After doing so : Yapıp
     */
    AFTERDOINGSO,
    /**
     * Almost : Dikensi
     */
    ALMOST,
    /**
     * As : gibi
     */
    AS,
    /**
     * As if : Yaşarmışcasına
     */
    ASIF,
    /**
     * Become : Abideleş
     */
    BECOME,
    /**
     * Ever since : Çıkagel
     */
    EVERSINCE,
    /**
     * Projection : Öpülesi
     */
    FEELLIKE,
    /**
     * Hastility : Yapıver
     */
    HASTILY,
    /**
     * In between : Arasında
     */
    INBETWEEN,
    /**
     * Just like : Destansı
     */
    JUSTLIKE,
    /**
     * -LY : Akıllıca
     */
    LY,
    /**
     * Related to : Davranışsal
     */
    RELATED,
    /**
     * Continuous : Yapadur
     */
    REPEAT,
    /**
     * Since doing so : Amasyalı
     */
    SINCE,
    /**
     * Since doing so : Amasyalı
     */
    SINCEDOINGSO,
    /**
     * Start : Alıkoy
     */
    START,
    /**
     * Stay : Bakakal
     */
    STAY,
    /**
     * Equative : Öylece
     */
    EQUATIVE,
    /**
     * Instrumental : Kışın, arabayla
     */
    INSTRUMENTAL,
    /**
     * Aorist Tense : Her hafta sonunda futbol oynarlar.
     */
    AORIST,
    /**
     * Desire/Past Auxiliary : Çıkarsa
     */
    DESIRE,
    /**
     * Future : Yağacak
     */
    FUTURE,
    /**
     * Imperative : Otur!
     */
    IMPERATIVE,
    /**
     * Narrative Past Tense : Oluşmuş
     */
    NARRATIVE,
    /**
     * Necessity : Yapmalı
     */
    NECESSITY,
    /**
     * Optative : Doğanaya
     */
    OPTATIVE,
    /**
     * Past tense : Gitti
     */
    PAST,
    /**
     * Present partıcıple : Sarılan
     */
    PRESENT,
    /**
     * Progressive : Görüyorum
     */
    PROGRESSIVE1,
    /**
     * Progressive : Görmekteyim
     */
    PROGRESSIVE2,
    /**
     * Conditional : Gelirse
     */
    CONDITIONAL,
    /**
     * Copula : Mavidir
     */
    COPULA,
    /**
     * Positive : Gittim
     */
    POSITIVE,
    /**
     * Pronoun : Ben
     */
    PRONOUN,
    /**
     * Locative : Aşağıda
     */
    LOCATIVE,
    /**
     * Relative : Gelenin
     */
    RELATIVE,
    /**
     * Demonstrative : Bu
     */
    DEMONSTRATIVE,
    /**
     * Infinitive2 : Gitme
     */
    INFINITIVE2,
    /**
     * Infinitive3 : Gidiş
     */
    INFINITIVE3,
    /**
     * Sentence beginning header
     */
    BEGINNINGOFSENTENCE,
    /**
     * Sentence ending header
     */
    ENDOFSENTENCE,
    /**
     * Title beginning header
     */
    BEGINNINGOFTITLE,
    /**
     * Title ending header
     */
    ENDOFTITLE,
    /**
     * Document beginning header
     */
    BEGINNINGOFDOCUMENT,
    /**
     * Document ending header
     */
    ENDOFDOCUMENT,
    /**
     * As long as : Yaşadıkça
     */
    ASLONGAS,
    /**
     * Adamantly
     */
    ADAMANTLY,
    /**
     * Percent : 15%
     */
    PERCENT,
    /**
     * Without being able to have done so: kararlamadan
     */
    WITHOUTBEINGABLETOHAVEDONESO,
    /**
     * Dimension : Küçücük
     */
    DIMENSION,
    /**
     * Notable state : Anlaşılmazlık
     */
    NOTABLESTATE,
    /**
     * Fraction : 1/2
     */
    FRACTION,
    /**
     * Hash tag : #
     */
    HASHTAG,
    /**
     * E-mail : @
     */
    EMAIL,
    /**
     * Date : 11/06/2018
     */
    DATE,
    /**
     * Code : i7-9700K
     */
    CODE,
    /**
     * Metric : 6cmx7cmx8cm
     */
    METRIC,
    /**
     * Polite: yapınız, gidiniz
     */
    POLITE,
    /**
     * Urge: Baksana, yapsana
     */
    URGE
}