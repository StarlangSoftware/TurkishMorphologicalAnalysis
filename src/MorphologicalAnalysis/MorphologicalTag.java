package MorphologicalAnalysis;

/*
        A1PL                    Agreement 1st person plural
        A1SG                    Agreement 1st person singular
        A2PL                    Agreement 2nd person plural
        A2SG                    Agreement 2nd person singular
        A3PL                    Agreement 3rd person plural
        A3SG                    Agreement 3rd person singular
        ABL                     Ablative Case
        ABLE                    Ability, possibility (-(y)Abil)
        ACC                     Accusative Case
        ACQUIRE                 Acquire (-lAn)
        ADJ                     Cat: Adjective
        ADV                     Cat: Adverb
        AFTERDOINGSO            After doing so (-(y)Ip)
        AGT	                    Agent (-CI, -(y)IcI)
        ALMOST	                -sI
        AOR	                    Aorist Tense
        ASIF	                -CA, CAsInA
        ASLONGAS	            -dIkçA
        BECOME	                Become (-lAş)
        BYDOINGSO	            By doing so (-(y)ArAk)
        CARD	                Cardinal number
        CAUS	                Causative
        COND	                Conditional (-(y)sA)
        CONJ	                Cat: Conjunction or disjunction
        COP	                    Copula (-DIr)
        DAT	                    Dative Case
        DATE	                Date (xx.yy.zzzz)
        DEMONSP	                Demonstrative Pronoun
        DESR	                Desire/Past Auxiliary (-sA)
        DET	                    Cat: Determiner
        DIM	                    Diminutive (-CIk)
        DISTRIB	                Cardinal-to-distribution (-şAr)
        DUP	                    Duplication
        EMAIL	                e-mail (x@y)
        EQU	                    -(I)ncA (by phrase)
        EVERSINCE	            Ever since (-(y)Agel)
        FEELLIKE	            Projection (-(y)AsI) e.g. öpülesi
        FITFOR	                -(A)lIk ??
        FUT	                    Future Tense (-(y)AcAk)
        FUTPART	                Future Participle (-(y)AcAk)
        GEN	                    Genitive CASE
        HASTILY	                Hastility (-(y)Iver)
        IMP	                    Imperative (0, -sın, -(y)In, -sInlAr)
        INF	                    Infinitive (-mAK)
        INF2	                Infinitive 2 (-mA)
        INF3	                Infinitive 3 (-y)Iş
        INS	                    Instrumental
        INTERJ	                Cat: Interjection
        JUSTLIKE	            -sI
        LOC	                    Locative Case
        LY	                    -ly (-CA)
        NARR	                Narrative Past Tens (-mIş)
        NECES	                Necessitative/Obligative (-mAlI)
        NEG	                    Negative
        NESS	                State of (-(I)lIk)
        NOM	                    Nominative Case
        NOUN	                Cat: Noun
        NUM	                    Cat: Numeral/Number
        OPT	                    Optative (-(y)A)
        ORD	                    Ordinal Number
        P1PL	                Possessive 1st person plural
        P1SG	                Possessive 1st person singular
        P2PL	                Possessive 2nd person plural
        P2SG	                Possessive 2nd person singular
        P3PL	                Possessive 3rd person plural
        P3SG	                Possessive 3rd person singular
        PASS	                Passive (-Il, -(I)n)
        PAST	                Past Tense (-DI)
        PASTPART	            Past participle (-DIK)
        PCABL	                Postp. w/ Ablative complement
        PCACC	                Postp. w/ Accusative complement
        PCDAT	                Postp. w/ Dative Complement
        PCINS	                Postp. w/ Instrumental Complement
        PCNOM	                Postp. w/ Nominative Complement
        PERS	                Personal pronoun
        PNON	                None possessive
        POS	                    Positive (verbs)
        POSTP	                Postposition
        PRES	                Present Tense (0)
        PRESPART	            Present Participle (-(y)An)
        PROG1	                Progressive Tense 1 (-(I)yor)
        PROG2	                Progressive Tense 2 (-mAktA)
        PRON	                Cat: Pronoun
        PROP	                Proper Noun
        PUNC	                Punctuation
        QUANTP	                Quantifier (Quantificational Pronoun)?
        QUES	                mI
        QUESP	                Question Particle (ne, kim, nasıl ...)
        RANGE	                Range (e.g. 6-7)
        RECIP	                Reciprocal (-(I)ş)
        REFLEX	                Reflexive (-(I)n)
        REL	                    Relativizer (-ki)
        RELATED	                Related to (-sAl)
        REPEAT	                Continuous (-(y)Adur)
        SINCE	                Since (-dIr)
        SINCEDOINGSO	        Since doing so (-(y)AlI)
        START	                Start (-(y)Akoy)
        STAY	                Stay (-(y)Akal)
        TIME	                Time (xx:yy)
        VERB	                Cat: Verb
        WHEN	                When (-(y)IncA)
        WHILE	                While (-(y)ken)
        WITH	                With (-lI)
        WITHOUT	                Without (-sIz)
        WITHOUTHAVINGDONESO     Without having done so (-mAdAn)
        ZERO	                Zero Derivation
*/

public enum MorphologicalTag {
    NOUN, ADVERB, ADJECTIVE, VERB, A1SG,
    A2SG, A3SG, A1PL, A2PL, A3PL,
    P1SG, P2SG, P3SG, P1PL, P2PL,
    P3PL, PROPERNOUN, PNON, NOMINATIVE, WITH,
    WITHOUT, ACCUSATIVE, DATIVE, GENITIVE, ABLATIVE,
    PERSONALPRONOUN, ZERO, ABLE, NEGATIVE, PASTTENSE,
    CONJUNCTION, DETERMINER, DUPLICATION, INTERJECTION, NUMBER,
    POSTPOSITION, PUNCTUATION, QUESTION, AGENT, BYDOINGSO,
    CARDINAL, CAUSATIVE, DEMONSTRATIVEPRONOUN, DISTRIBUTIVE, FITFOR,
    FUTUREPARTICIPLE, INFINITIVE, NESS, ORDINAL, PASSIVE,
    PASTPARTICIPLE, PRESENTPARTICIPLE, QUESTIONPRONOUN, QUANTITATIVEPRONOUN, RANGE,
    RATIO, REAL, RECIPROCAL, REFLEXIVE, REFLEXIVEPRONOUN,
    TIME, WHEN, WHILE, WITHOUTHAVINGDONESO, PCABLATIVE,
    PCACCUSATIVE, PCDATIVE, PCGENITIVE, PCINSTRUMENTAL, PCNOMINATIVE,
    ACQUIRE, ACTOF, AFTERDOINGSO, ALMOST, AS,
    ASIF, BECOME, EVERSINCE, FEELLIKE, HASTILY,
    INBETWEEN, JUSTLIKE, LY, RELATED, REPEAT,
    SINCE, SINCEDOINGSO, START, STAY,
    EQUATIVE, INSTRUMENTAL, AORIST, DESIRE, FUTURE,
    IMPERATIVE, NARRATIVE, NECESSITY, OPTATIVE, PAST,
    PRESENT, PROGRESSIVE1, PROGRESSIVE2, CONDITIONAL, COPULA,
    POSITIVE, PRONOUN, LOCATIVE, RELATIVE, DEMONSTRATIVE,
    INFINITIVE2, INFINITIVE3, BEGINNINGOFSENTENCE, ENDOFSENTENCE, BEGINNINGOFTITLE,
    ENDOFTITLE, BEGINNINGOFDOCUMENT, ENDOFDOCUMENT, ASLONGAS, ADAMANTLY,
    PERCENT, WITHOUTBEINGABLETOHAVEDONESO, DIMENSION, NOTABLESTATE, FRACTION,
    HASHTAG, EMAIL, DATE
}
