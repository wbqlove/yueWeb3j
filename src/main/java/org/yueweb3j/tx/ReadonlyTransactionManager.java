/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.yueweb3j.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.yueweb3j.protocol.Web3j;
import org.yueweb3j.protocol.core.DefaultBlockParameter;
import org.yueweb3j.protocol.core.methods.request.Transaction;
import org.yueweb3j.protocol.core.methods.response.YueCall;
import org.yueweb3j.protocol.core.methods.response.YueGetCode;
import org.yueweb3j.protocol.core.methods.response.YueSendTransaction;

/** Transaction manager implementation for read-only operations on smart contracts. */
public class ReadonlyTransactionManager extends TransactionManager {
    private final Web3j web3j;
    private String fromAddress;

    public ReadonlyTransactionManager(Web3j web3j, String fromAddress) {
        super(web3j, fromAddress);
        this.web3j = web3j;
        this.fromAddress = fromAddress;
    }

    @Override
    public YueSendTransaction sendTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            boolean constructor)
            throws IOException {
        throw new UnsupportedOperationException(
                "Only read operations are supported by this transaction manager");
    }

    @Override
    public YueSendTransaction sendTransactionEIP1559(
            BigInteger gasPremium,
            BigInteger feeCap,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            boolean constructor)
            throws IOException {
        throw new UnsupportedOperationException(
                "Only read operations are supported by this transaction manager");
    }

    @Override
    public String sendCall(String to, String data, DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        YueCall yueCall =
                web3j.yueCall(
                                Transaction.createEthCallTransaction(fromAddress, to, data),
                                defaultBlockParameter)
                        .send();

        assertCallNotReverted(yueCall);
        return yueCall.getValue();
    }

    @Override
    public YueGetCode getCode(
            final String contractAddress, final DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        return web3j.yueGetCode(contractAddress, defaultBlockParameter).send();
    }
}
