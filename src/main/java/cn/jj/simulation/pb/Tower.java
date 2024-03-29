// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: tower.proto

package cn.jj.simulation.pb;

public final class Tower {
  private Tower() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MsgTowerOrBuilder extends
      // @@protoc_insertion_point(interface_extends:shuguang.MsgTower)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * 局内唯一的ID (必填)
     * </pre>
     *
     * <code>int64 Id = 1;</code>
     * @return The id.
     */
    long getId();

    /**
     * <pre>
     * 阵营
     * </pre>
     *
     * <code>int32 campId = 2;</code>
     * @return The campId.
     */
    int getCampId();

    /**
     * <pre>
     * 塔的类型（业务补充有哪些种类）
     * </pre>
     *
     * <code>int32 type = 3;</code>
     * @return The type.
     */
    int getType();

    /**
     * <pre>
     * 位置
     * </pre>
     *
     * <code>int32 posX = 4;</code>
     * @return The posX.
     */
    int getPosX();

    /**
     * <pre>
     * 位置
     * </pre>
     *
     * <code>int32 posY = 5;</code>
     * @return The posY.
     */
    int getPosY();

    /**
     * <pre>
     * 血量
     * </pre>
     *
     * <code>int32 hp = 6;</code>
     * @return The hp.
     */
    int getHp();

    /**
     * <pre>
     * 最大血量
     * </pre>
     *
     * <code>int32 hpMax = 7;</code>
     * @return The hpMax.
     */
    int getHpMax();

    /**
     * <pre>
     * 攻击力
     * </pre>
     *
     * <code>int32 atk = 8;</code>
     * @return The atk.
     */
    int getAtk();

    /**
     * <pre>
     * 防御能力
     * </pre>
     *
     * <code>int32 def = 9;</code>
     * @return The def.
     */
    int getDef();

    /**
     * <pre>
     * 攻击目标
     * </pre>
     *
     * <code>int64 targetId = 10;</code>
     * @return The targetId.
     */
    long getTargetId();

    /**
     * <code>int32 atkRange = 11;</code>
     * @return The atkRange.
     */
    int getAtkRange();

    /**
     * <code>int32 towerId = 12;</code>
     * @return The towerId.
     */
    int getTowerId();
  }
  /**
   * <pre>
   * 塔的基础属性
   * </pre>
   *
   * Protobuf type {@code shuguang.MsgTower}
   */
  public static final class MsgTower extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:shuguang.MsgTower)
      MsgTowerOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use MsgTower.newBuilder() to construct.
    private MsgTower(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private MsgTower() {
    }

    @Override
    @SuppressWarnings({"unused"})
    protected Object newInstance(
        UnusedPrivateParameter unused) {
      return new MsgTower();
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private MsgTower(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new NullPointerException();
      }
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {

              id_ = input.readInt64();
              break;
            }
            case 16: {

              campId_ = input.readInt32();
              break;
            }
            case 24: {

              type_ = input.readInt32();
              break;
            }
            case 32: {

              posX_ = input.readInt32();
              break;
            }
            case 40: {

              posY_ = input.readInt32();
              break;
            }
            case 48: {

              hp_ = input.readInt32();
              break;
            }
            case 56: {

              hpMax_ = input.readInt32();
              break;
            }
            case 64: {

              atk_ = input.readInt32();
              break;
            }
            case 72: {

              def_ = input.readInt32();
              break;
            }
            case 80: {

              targetId_ = input.readInt64();
              break;
            }
            case 88: {

              atkRange_ = input.readInt32();
              break;
            }
            case 96: {

              towerId_ = input.readInt32();
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return Tower.internal_static_shuguang_MsgTower_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return Tower.internal_static_shuguang_MsgTower_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              MsgTower.class, Builder.class);
    }

    public static final int ID_FIELD_NUMBER = 1;
    private long id_;
    /**
     * <pre>
     * 局内唯一的ID (必填)
     * </pre>
     *
     * <code>int64 Id = 1;</code>
     * @return The id.
     */
    @Override
    public long getId() {
      return id_;
    }

    public static final int CAMPID_FIELD_NUMBER = 2;
    private int campId_;
    /**
     * <pre>
     * 阵营
     * </pre>
     *
     * <code>int32 campId = 2;</code>
     * @return The campId.
     */
    @Override
    public int getCampId() {
      return campId_;
    }

    public static final int TYPE_FIELD_NUMBER = 3;
    private int type_;
    /**
     * <pre>
     * 塔的类型（业务补充有哪些种类）
     * </pre>
     *
     * <code>int32 type = 3;</code>
     * @return The type.
     */
    @Override
    public int getType() {
      return type_;
    }

    public static final int POSX_FIELD_NUMBER = 4;
    private int posX_;
    /**
     * <pre>
     * 位置
     * </pre>
     *
     * <code>int32 posX = 4;</code>
     * @return The posX.
     */
    @Override
    public int getPosX() {
      return posX_;
    }

    public static final int POSY_FIELD_NUMBER = 5;
    private int posY_;
    /**
     * <pre>
     * 位置
     * </pre>
     *
     * <code>int32 posY = 5;</code>
     * @return The posY.
     */
    @Override
    public int getPosY() {
      return posY_;
    }

    public static final int HP_FIELD_NUMBER = 6;
    private int hp_;
    /**
     * <pre>
     * 血量
     * </pre>
     *
     * <code>int32 hp = 6;</code>
     * @return The hp.
     */
    @Override
    public int getHp() {
      return hp_;
    }

    public static final int HPMAX_FIELD_NUMBER = 7;
    private int hpMax_;
    /**
     * <pre>
     * 最大血量
     * </pre>
     *
     * <code>int32 hpMax = 7;</code>
     * @return The hpMax.
     */
    @Override
    public int getHpMax() {
      return hpMax_;
    }

    public static final int ATK_FIELD_NUMBER = 8;
    private int atk_;
    /**
     * <pre>
     * 攻击力
     * </pre>
     *
     * <code>int32 atk = 8;</code>
     * @return The atk.
     */
    @Override
    public int getAtk() {
      return atk_;
    }

    public static final int DEF_FIELD_NUMBER = 9;
    private int def_;
    /**
     * <pre>
     * 防御能力
     * </pre>
     *
     * <code>int32 def = 9;</code>
     * @return The def.
     */
    @Override
    public int getDef() {
      return def_;
    }

    public static final int TARGETID_FIELD_NUMBER = 10;
    private long targetId_;
    /**
     * <pre>
     * 攻击目标
     * </pre>
     *
     * <code>int64 targetId = 10;</code>
     * @return The targetId.
     */
    @Override
    public long getTargetId() {
      return targetId_;
    }

    public static final int ATKRANGE_FIELD_NUMBER = 11;
    private int atkRange_;
    /**
     * <code>int32 atkRange = 11;</code>
     * @return The atkRange.
     */
    @Override
    public int getAtkRange() {
      return atkRange_;
    }

    public static final int TOWERID_FIELD_NUMBER = 12;
    private int towerId_;
    /**
     * <code>int32 towerId = 12;</code>
     * @return The towerId.
     */
    @Override
    public int getTowerId() {
      return towerId_;
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (id_ != 0L) {
        output.writeInt64(1, id_);
      }
      if (campId_ != 0) {
        output.writeInt32(2, campId_);
      }
      if (type_ != 0) {
        output.writeInt32(3, type_);
      }
      if (posX_ != 0) {
        output.writeInt32(4, posX_);
      }
      if (posY_ != 0) {
        output.writeInt32(5, posY_);
      }
      if (hp_ != 0) {
        output.writeInt32(6, hp_);
      }
      if (hpMax_ != 0) {
        output.writeInt32(7, hpMax_);
      }
      if (atk_ != 0) {
        output.writeInt32(8, atk_);
      }
      if (def_ != 0) {
        output.writeInt32(9, def_);
      }
      if (targetId_ != 0L) {
        output.writeInt64(10, targetId_);
      }
      if (atkRange_ != 0) {
        output.writeInt32(11, atkRange_);
      }
      if (towerId_ != 0) {
        output.writeInt32(12, towerId_);
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (id_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(1, id_);
      }
      if (campId_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, campId_);
      }
      if (type_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, type_);
      }
      if (posX_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(4, posX_);
      }
      if (posY_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(5, posY_);
      }
      if (hp_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(6, hp_);
      }
      if (hpMax_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(7, hpMax_);
      }
      if (atk_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(8, atk_);
      }
      if (def_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(9, def_);
      }
      if (targetId_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(10, targetId_);
      }
      if (atkRange_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(11, atkRange_);
      }
      if (towerId_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(12, towerId_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof MsgTower)) {
        return super.equals(obj);
      }
      MsgTower other = (MsgTower) obj;

      if (getId()
          != other.getId()) return false;
      if (getCampId()
          != other.getCampId()) return false;
      if (getType()
          != other.getType()) return false;
      if (getPosX()
          != other.getPosX()) return false;
      if (getPosY()
          != other.getPosY()) return false;
      if (getHp()
          != other.getHp()) return false;
      if (getHpMax()
          != other.getHpMax()) return false;
      if (getAtk()
          != other.getAtk()) return false;
      if (getDef()
          != other.getDef()) return false;
      if (getTargetId()
          != other.getTargetId()) return false;
      if (getAtkRange()
          != other.getAtkRange()) return false;
      if (getTowerId()
          != other.getTowerId()) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getId());
      hash = (37 * hash) + CAMPID_FIELD_NUMBER;
      hash = (53 * hash) + getCampId();
      hash = (37 * hash) + TYPE_FIELD_NUMBER;
      hash = (53 * hash) + getType();
      hash = (37 * hash) + POSX_FIELD_NUMBER;
      hash = (53 * hash) + getPosX();
      hash = (37 * hash) + POSY_FIELD_NUMBER;
      hash = (53 * hash) + getPosY();
      hash = (37 * hash) + HP_FIELD_NUMBER;
      hash = (53 * hash) + getHp();
      hash = (37 * hash) + HPMAX_FIELD_NUMBER;
      hash = (53 * hash) + getHpMax();
      hash = (37 * hash) + ATK_FIELD_NUMBER;
      hash = (53 * hash) + getAtk();
      hash = (37 * hash) + DEF_FIELD_NUMBER;
      hash = (53 * hash) + getDef();
      hash = (37 * hash) + TARGETID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getTargetId());
      hash = (37 * hash) + ATKRANGE_FIELD_NUMBER;
      hash = (53 * hash) + getAtkRange();
      hash = (37 * hash) + TOWERID_FIELD_NUMBER;
      hash = (53 * hash) + getTowerId();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static MsgTower parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static MsgTower parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static MsgTower parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static MsgTower parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static MsgTower parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static MsgTower parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static MsgTower parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static MsgTower parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static MsgTower parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static MsgTower parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static MsgTower parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static MsgTower parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(MsgTower prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     * 塔的基础属性
     * </pre>
     *
     * Protobuf type {@code shuguang.MsgTower}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:shuguang.MsgTower)
        MsgTowerOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return Tower.internal_static_shuguang_MsgTower_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return Tower.internal_static_shuguang_MsgTower_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                MsgTower.class, Builder.class);
      }

      // Construct using cn.jj.shuguang.pb.Tower.MsgTower.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @Override
      public Builder clear() {
        super.clear();
        id_ = 0L;

        campId_ = 0;

        type_ = 0;

        posX_ = 0;

        posY_ = 0;

        hp_ = 0;

        hpMax_ = 0;

        atk_ = 0;

        def_ = 0;

        targetId_ = 0L;

        atkRange_ = 0;

        towerId_ = 0;

        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return Tower.internal_static_shuguang_MsgTower_descriptor;
      }

      @Override
      public MsgTower getDefaultInstanceForType() {
        return MsgTower.getDefaultInstance();
      }

      @Override
      public MsgTower build() {
        MsgTower result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public MsgTower buildPartial() {
        MsgTower result = new MsgTower(this);
        result.id_ = id_;
        result.campId_ = campId_;
        result.type_ = type_;
        result.posX_ = posX_;
        result.posY_ = posY_;
        result.hp_ = hp_;
        result.hpMax_ = hpMax_;
        result.atk_ = atk_;
        result.def_ = def_;
        result.targetId_ = targetId_;
        result.atkRange_ = atkRange_;
        result.towerId_ = towerId_;
        onBuilt();
        return result;
      }

      @Override
      public Builder clone() {
        return super.clone();
      }
      @Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.setField(field, value);
      }
      @Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.addRepeatedField(field, value);
      }
      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof MsgTower) {
          return mergeFrom((MsgTower)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(MsgTower other) {
        if (other == MsgTower.getDefaultInstance()) return this;
        if (other.getId() != 0L) {
          setId(other.getId());
        }
        if (other.getCampId() != 0) {
          setCampId(other.getCampId());
        }
        if (other.getType() != 0) {
          setType(other.getType());
        }
        if (other.getPosX() != 0) {
          setPosX(other.getPosX());
        }
        if (other.getPosY() != 0) {
          setPosY(other.getPosY());
        }
        if (other.getHp() != 0) {
          setHp(other.getHp());
        }
        if (other.getHpMax() != 0) {
          setHpMax(other.getHpMax());
        }
        if (other.getAtk() != 0) {
          setAtk(other.getAtk());
        }
        if (other.getDef() != 0) {
          setDef(other.getDef());
        }
        if (other.getTargetId() != 0L) {
          setTargetId(other.getTargetId());
        }
        if (other.getAtkRange() != 0) {
          setAtkRange(other.getAtkRange());
        }
        if (other.getTowerId() != 0) {
          setTowerId(other.getTowerId());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        MsgTower parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (MsgTower) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private long id_ ;
      /**
       * <pre>
       * 局内唯一的ID (必填)
       * </pre>
       *
       * <code>int64 Id = 1;</code>
       * @return The id.
       */
      @Override
      public long getId() {
        return id_;
      }
      /**
       * <pre>
       * 局内唯一的ID (必填)
       * </pre>
       *
       * <code>int64 Id = 1;</code>
       * @param value The id to set.
       * @return This builder for chaining.
       */
      public Builder setId(long value) {
        
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 局内唯一的ID (必填)
       * </pre>
       *
       * <code>int64 Id = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearId() {
        
        id_ = 0L;
        onChanged();
        return this;
      }

      private int campId_ ;
      /**
       * <pre>
       * 阵营
       * </pre>
       *
       * <code>int32 campId = 2;</code>
       * @return The campId.
       */
      @Override
      public int getCampId() {
        return campId_;
      }
      /**
       * <pre>
       * 阵营
       * </pre>
       *
       * <code>int32 campId = 2;</code>
       * @param value The campId to set.
       * @return This builder for chaining.
       */
      public Builder setCampId(int value) {
        
        campId_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 阵营
       * </pre>
       *
       * <code>int32 campId = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearCampId() {
        
        campId_ = 0;
        onChanged();
        return this;
      }

      private int type_ ;
      /**
       * <pre>
       * 塔的类型（业务补充有哪些种类）
       * </pre>
       *
       * <code>int32 type = 3;</code>
       * @return The type.
       */
      @Override
      public int getType() {
        return type_;
      }
      /**
       * <pre>
       * 塔的类型（业务补充有哪些种类）
       * </pre>
       *
       * <code>int32 type = 3;</code>
       * @param value The type to set.
       * @return This builder for chaining.
       */
      public Builder setType(int value) {
        
        type_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 塔的类型（业务补充有哪些种类）
       * </pre>
       *
       * <code>int32 type = 3;</code>
       * @return This builder for chaining.
       */
      public Builder clearType() {
        
        type_ = 0;
        onChanged();
        return this;
      }

      private int posX_ ;
      /**
       * <pre>
       * 位置
       * </pre>
       *
       * <code>int32 posX = 4;</code>
       * @return The posX.
       */
      @Override
      public int getPosX() {
        return posX_;
      }
      /**
       * <pre>
       * 位置
       * </pre>
       *
       * <code>int32 posX = 4;</code>
       * @param value The posX to set.
       * @return This builder for chaining.
       */
      public Builder setPosX(int value) {
        
        posX_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 位置
       * </pre>
       *
       * <code>int32 posX = 4;</code>
       * @return This builder for chaining.
       */
      public Builder clearPosX() {
        
        posX_ = 0;
        onChanged();
        return this;
      }

      private int posY_ ;
      /**
       * <pre>
       * 位置
       * </pre>
       *
       * <code>int32 posY = 5;</code>
       * @return The posY.
       */
      @Override
      public int getPosY() {
        return posY_;
      }
      /**
       * <pre>
       * 位置
       * </pre>
       *
       * <code>int32 posY = 5;</code>
       * @param value The posY to set.
       * @return This builder for chaining.
       */
      public Builder setPosY(int value) {
        
        posY_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 位置
       * </pre>
       *
       * <code>int32 posY = 5;</code>
       * @return This builder for chaining.
       */
      public Builder clearPosY() {
        
        posY_ = 0;
        onChanged();
        return this;
      }

      private int hp_ ;
      /**
       * <pre>
       * 血量
       * </pre>
       *
       * <code>int32 hp = 6;</code>
       * @return The hp.
       */
      @Override
      public int getHp() {
        return hp_;
      }
      /**
       * <pre>
       * 血量
       * </pre>
       *
       * <code>int32 hp = 6;</code>
       * @param value The hp to set.
       * @return This builder for chaining.
       */
      public Builder setHp(int value) {
        
        hp_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 血量
       * </pre>
       *
       * <code>int32 hp = 6;</code>
       * @return This builder for chaining.
       */
      public Builder clearHp() {
        
        hp_ = 0;
        onChanged();
        return this;
      }

      private int hpMax_ ;
      /**
       * <pre>
       * 最大血量
       * </pre>
       *
       * <code>int32 hpMax = 7;</code>
       * @return The hpMax.
       */
      @Override
      public int getHpMax() {
        return hpMax_;
      }
      /**
       * <pre>
       * 最大血量
       * </pre>
       *
       * <code>int32 hpMax = 7;</code>
       * @param value The hpMax to set.
       * @return This builder for chaining.
       */
      public Builder setHpMax(int value) {
        
        hpMax_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 最大血量
       * </pre>
       *
       * <code>int32 hpMax = 7;</code>
       * @return This builder for chaining.
       */
      public Builder clearHpMax() {
        
        hpMax_ = 0;
        onChanged();
        return this;
      }

      private int atk_ ;
      /**
       * <pre>
       * 攻击力
       * </pre>
       *
       * <code>int32 atk = 8;</code>
       * @return The atk.
       */
      @Override
      public int getAtk() {
        return atk_;
      }
      /**
       * <pre>
       * 攻击力
       * </pre>
       *
       * <code>int32 atk = 8;</code>
       * @param value The atk to set.
       * @return This builder for chaining.
       */
      public Builder setAtk(int value) {
        
        atk_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 攻击力
       * </pre>
       *
       * <code>int32 atk = 8;</code>
       * @return This builder for chaining.
       */
      public Builder clearAtk() {
        
        atk_ = 0;
        onChanged();
        return this;
      }

      private int def_ ;
      /**
       * <pre>
       * 防御能力
       * </pre>
       *
       * <code>int32 def = 9;</code>
       * @return The def.
       */
      @Override
      public int getDef() {
        return def_;
      }
      /**
       * <pre>
       * 防御能力
       * </pre>
       *
       * <code>int32 def = 9;</code>
       * @param value The def to set.
       * @return This builder for chaining.
       */
      public Builder setDef(int value) {
        
        def_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 防御能力
       * </pre>
       *
       * <code>int32 def = 9;</code>
       * @return This builder for chaining.
       */
      public Builder clearDef() {
        
        def_ = 0;
        onChanged();
        return this;
      }

      private long targetId_ ;
      /**
       * <pre>
       * 攻击目标
       * </pre>
       *
       * <code>int64 targetId = 10;</code>
       * @return The targetId.
       */
      @Override
      public long getTargetId() {
        return targetId_;
      }
      /**
       * <pre>
       * 攻击目标
       * </pre>
       *
       * <code>int64 targetId = 10;</code>
       * @param value The targetId to set.
       * @return This builder for chaining.
       */
      public Builder setTargetId(long value) {
        
        targetId_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 攻击目标
       * </pre>
       *
       * <code>int64 targetId = 10;</code>
       * @return This builder for chaining.
       */
      public Builder clearTargetId() {
        
        targetId_ = 0L;
        onChanged();
        return this;
      }

      private int atkRange_ ;
      /**
       * <code>int32 atkRange = 11;</code>
       * @return The atkRange.
       */
      @Override
      public int getAtkRange() {
        return atkRange_;
      }
      /**
       * <code>int32 atkRange = 11;</code>
       * @param value The atkRange to set.
       * @return This builder for chaining.
       */
      public Builder setAtkRange(int value) {
        
        atkRange_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 atkRange = 11;</code>
       * @return This builder for chaining.
       */
      public Builder clearAtkRange() {
        
        atkRange_ = 0;
        onChanged();
        return this;
      }

      private int towerId_ ;
      /**
       * <code>int32 towerId = 12;</code>
       * @return The towerId.
       */
      @Override
      public int getTowerId() {
        return towerId_;
      }
      /**
       * <code>int32 towerId = 12;</code>
       * @param value The towerId to set.
       * @return This builder for chaining.
       */
      public Builder setTowerId(int value) {
        
        towerId_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 towerId = 12;</code>
       * @return This builder for chaining.
       */
      public Builder clearTowerId() {
        
        towerId_ = 0;
        onChanged();
        return this;
      }
      @Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:shuguang.MsgTower)
    }

    // @@protoc_insertion_point(class_scope:shuguang.MsgTower)
    private static final MsgTower DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new MsgTower();
    }

    public static MsgTower getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<MsgTower>
        PARSER = new com.google.protobuf.AbstractParser<MsgTower>() {
      @Override
      public MsgTower parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MsgTower(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<MsgTower> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<MsgTower> getParserForType() {
      return PARSER;
    }

    @Override
    public MsgTower getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_shuguang_MsgTower_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_shuguang_MsgTower_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\013tower.proto\022\010shuguang\"\272\001\n\010MsgTower\022\n\n\002" +
      "Id\030\001 \001(\003\022\016\n\006campId\030\002 \001(\005\022\014\n\004type\030\003 \001(\005\022\014" +
      "\n\004posX\030\004 \001(\005\022\014\n\004posY\030\005 \001(\005\022\n\n\002hp\030\006 \001(\005\022\r" +
      "\n\005hpMax\030\007 \001(\005\022\013\n\003atk\030\010 \001(\005\022\013\n\003def\030\t \001(\005\022" +
      "\020\n\010targetId\030\n \001(\003\022\020\n\010atkRange\030\013 \001(\005\022\017\n\007t" +
      "owerId\030\014 \001(\005B\023\n\021cn.jj.shuguang.pbb\006proto" +
      "3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_shuguang_MsgTower_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_shuguang_MsgTower_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_shuguang_MsgTower_descriptor,
        new String[] { "Id", "CampId", "Type", "PosX", "PosY", "Hp", "HpMax", "Atk", "Def", "TargetId", "AtkRange", "TowerId", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
